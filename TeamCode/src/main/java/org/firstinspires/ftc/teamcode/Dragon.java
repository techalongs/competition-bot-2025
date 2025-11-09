package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Dragon class implementing flight mechanics and fire-breathing capabilities.
 * Uses SolversLib for motor and servo control to ensure compatibility with
 * the competition bot framework.
 * 
 * Features:
 * - Flight mechanics with altitude control
 * - Fire-breathing with configurable range and damage
 * - Peasant targeting system
 * - Animation and timing control
 */
public class Dragon {
    private final MotorEx leftWingMotor;
    private final MotorEx rightWingMotor;
    private final MotorEx verticalThrustMotor;
    private final ServoEx headServo;
    private final ServoEx fireBreathServo;
    
    private final Telemetry telemetry;
    private final ElapsedTime flightTimer;
    private final ElapsedTime fireBreathTimer;
    
    // Flight configuration
    private static final double MAX_FLIGHT_ALTITUDE = 100.0; // inches above terrain
    private static final double FLIGHT_SPEED = 0.8; // normalized power (0-1)
    private static final double HOVER_POWER = 0.3; // power needed to maintain altitude
    
    // Fire-breathing configuration
    private static final double FIRE_BREATH_RANGE = 48.0; // inches
    private static final double FIRE_BREATH_DURATION = 2.0; // seconds
    private static final double FIRE_BREATH_DAMAGE = 50.0; // damage units per second
    private static final double FIRE_BREATH_COOLDOWN = 3.0; // seconds between breaths
    
    // State tracking
    private boolean isFlying;
    private double currentAltitude;
    private boolean isBreathingFire;
    private double lastFireBreathTime;
    
    // Targeting
    private double targetX;
    private double targetY;
    private double targetRange;
    
    /**
     * Creates a new Dragon with flight and fire-breathing capabilities.
     * 
     * @param hardwareMap The robot's hardware map for accessing motors and servos
     * @param telemetry Telemetry for status reporting
     */
    public Dragon(HardwareMap hardwareMap, Telemetry telemetry) {
        // Initialize wing motors for flight control
        this.leftWingMotor = new MotorEx(hardwareMap, "dragonLeftWing", Motor.GoBILDA.RPM_435);
        this.rightWingMotor = new MotorEx(hardwareMap, "dragonRightWing", Motor.GoBILDA.RPM_435);
        this.verticalThrustMotor = new MotorEx(hardwareMap, "dragonVerticalThrust", Motor.GoBILDA.RPM_312);
        
        // Configure motors
        leftWingMotor.setInverted(false);
        rightWingMotor.setInverted(true); // Right wing inverted for symmetrical flight
        verticalThrustMotor.setInverted(false);
        
        leftWingMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightWingMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        verticalThrustMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        
        leftWingMotor.setRunMode(Motor.RunMode.RawPower);
        rightWingMotor.setRunMode(Motor.RunMode.RawPower);
        verticalThrustMotor.setRunMode(Motor.RunMode.RawPower);
        
        // Initialize servos for head control and fire breath mechanism
        this.headServo = new ServoEx(hardwareMap, "dragonHead");
        this.fireBreathServo = new ServoEx(hardwareMap, "dragonFireBreath");
        
        this.telemetry = telemetry;
        this.flightTimer = new ElapsedTime();
        this.fireBreathTimer = new ElapsedTime();
        
        // Initialize state
        this.isFlying = false;
        this.currentAltitude = 0.0;
        this.isBreathingFire = false;
        this.lastFireBreathTime = 0.0;
        
        telemetry.addData("Dragon", "Initialized and ready");
        telemetry.update();
    }
    
    /**
     * Initiates dragon flight, lifting off the terrain.
     * Uses vertical thrust motor to achieve and maintain altitude.
     */
    public void takeOff() {
        if (!isFlying) {
            isFlying = true;
            flightTimer.reset();
            telemetry.addData("Dragon", "Taking off!");
            telemetry.update();
        }
    }
    
    /**
     * Lands the dragon, descending to ground level.
     */
    public void land() {
        if (isFlying) {
            isFlying = false;
            currentAltitude = 0.0;
            stopAllMotors();
            telemetry.addData("Dragon", "Landing");
            telemetry.update();
        }
    }
    
    /**
     * Updates flight mechanics. Should be called repeatedly in a loop.
     * Maintains altitude and handles wing movement for flight.
     */
    public void updateFlight() {
        if (!isFlying) {
            stopAllMotors();
            return;
        }
        
        // Calculate target altitude based on flight time (ascend gradually)
        double targetAltitude = Math.min(flightTimer.seconds() * 20.0, MAX_FLIGHT_ALTITUDE);
        
        // Simple altitude control: increase or decrease thrust based on altitude error
        double altitudeError = targetAltitude - currentAltitude;
        double thrustPower;
        
        if (Math.abs(altitudeError) < 5.0) {
            // Near target altitude, use hover power
            thrustPower = HOVER_POWER;
        } else if (altitudeError > 0) {
            // Need to climb
            thrustPower = HOVER_POWER + (altitudeError / MAX_FLIGHT_ALTITUDE) * (FLIGHT_SPEED - HOVER_POWER);
        } else {
            // Need to descend
            thrustPower = HOVER_POWER - (Math.abs(altitudeError) / MAX_FLIGHT_ALTITUDE) * HOVER_POWER;
        }
        
        // Apply thrust
        verticalThrustMotor.set(thrustPower);
        
        // Wing flapping for forward flight (oscillating pattern)
        double wingPower = FLIGHT_SPEED * Math.sin(flightTimer.seconds() * 3.0);
        leftWingMotor.set(wingPower);
        rightWingMotor.set(wingPower);
        
        // Update current altitude (simplified physics model)
        currentAltitude += (thrustPower - HOVER_POWER) * 2.0;
        currentAltitude = Math.max(0.0, Math.min(currentAltitude, MAX_FLIGHT_ALTITUDE));
        
        telemetry.addData("Dragon Flight", "Active");
        telemetry.addData("Altitude", "%.1f inches", currentAltitude);
        telemetry.addData("Target Altitude", "%.1f inches", targetAltitude);
    }
    
    /**
     * Activates fire-breathing attack targeting peasants.
     * Checks cooldown and executes fire breath animation and damage.
     * 
     * @return true if fire breath was activated, false if on cooldown
     */
    public boolean breatheFire() {
        double currentTime = fireBreathTimer.seconds();
        
        // Check if fire breath is on cooldown
        if (currentTime - lastFireBreathTime < FIRE_BREATH_COOLDOWN) {
            telemetry.addData("Dragon", "Fire breath on cooldown");
            double remaining = FIRE_BREATH_COOLDOWN - (currentTime - lastFireBreathTime);
            telemetry.addData("Cooldown", "%.1f seconds remaining", remaining);
            return false;
        }
        
        // Activate fire breath
        isBreathingFire = true;
        lastFireBreathTime = currentTime;
        fireBreathTimer.reset();
        
        // Position head servo for fire breathing
        headServo.setPosition(0.5); // Center position for forward fire breath
        
        telemetry.addData("Dragon", "BREATHING FIRE!");
        telemetry.addData("Fire Range", "%.1f inches", FIRE_BREATH_RANGE);
        telemetry.addData("Fire Damage", "%.1f damage/sec", FIRE_BREATH_DAMAGE);
        telemetry.update();
        
        return true;
    }
    
    /**
     * Updates fire-breathing animation and damage calculation.
     * Should be called repeatedly in a loop when fire breath is active.
     */
    public void updateFireBreath() {
        if (!isBreathingFire) {
            fireBreathServo.setPosition(0.0); // Close fire breath valve
            return;
        }
        
        double fireBreathElapsed = fireBreathTimer.seconds();
        
        if (fireBreathElapsed >= FIRE_BREATH_DURATION) {
            // Fire breath duration complete
            isBreathingFire = false;
            fireBreathServo.setPosition(0.0);
            telemetry.addData("Dragon", "Fire breath complete");
            return;
        }
        
        // Animate fire breath (oscillating servo for visual effect)
        double breathIntensity = 0.5 + 0.5 * Math.sin(fireBreathElapsed * 10.0);
        fireBreathServo.setPosition(breathIntensity);
        
        // Calculate damage dealt to targets in range
        double damageThisFrame = FIRE_BREATH_DAMAGE * fireBreathElapsed / FIRE_BREATH_DURATION;
        
        telemetry.addData("Dragon", "Fire breathing...");
        telemetry.addData("Fire Duration", "%.1f / %.1f sec", fireBreathElapsed, FIRE_BREATH_DURATION);
        telemetry.addData("Breath Intensity", "%.0f%%", breathIntensity * 100);
        telemetry.addData("Damage Dealt", "%.1f", damageThisFrame);
    }
    
    /**
     * Targets a peasant at the specified coordinates.
     * Adjusts dragon head position to aim fire breath.
     * 
     * @param x X coordinate of target peasant
     * @param y Y coordinate of target peasant
     */
    public void targetPeasant(double x, double y) {
        this.targetX = x;
        this.targetY = y;
        this.targetRange = Math.sqrt(x * x + y * y);
        
        // Calculate head servo position based on target angle
        double targetAngle = Math.atan2(y, x);
        double servoPosition = 0.5 + (targetAngle / Math.PI) * 0.5; // Map angle to servo range
        servoPosition = Math.max(0.0, Math.min(1.0, servoPosition)); // Clamp to valid range
        
        headServo.setPosition(servoPosition);
        
        boolean inRange = targetRange <= FIRE_BREATH_RANGE;
        telemetry.addData("Dragon Target", "Peasant at (%.1f, %.1f)", x, y);
        telemetry.addData("Target Range", "%.1f inches", targetRange);
        telemetry.addData("In Fire Range", inRange ? "YES" : "NO");
    }
    
    /**
     * Checks if target is within fire breath range.
     * 
     * @return true if current target is within fire breath range
     */
    public boolean isTargetInRange() {
        return targetRange <= FIRE_BREATH_RANGE;
    }
    
    /**
     * Gets the current flight status.
     * 
     * @return true if dragon is currently flying
     */
    public boolean isFlying() {
        return isFlying;
    }
    
    /**
     * Gets the current altitude above terrain.
     * 
     * @return current altitude in inches
     */
    public double getAltitude() {
        return currentAltitude;
    }
    
    /**
     * Gets the fire breathing status.
     * 
     * @return true if dragon is currently breathing fire
     */
    public boolean isBreathingFire() {
        return isBreathingFire;
    }
    
    /**
     * Gets the fire breath cooldown remaining.
     * 
     * @return seconds until fire breath is ready again
     */
    public double getFireBreathCooldown() {
        double elapsed = fireBreathTimer.seconds() - lastFireBreathTime;
        return Math.max(0.0, FIRE_BREATH_COOLDOWN - elapsed);
    }
    
    /**
     * Performs a complete update cycle for all dragon systems.
     * Call this method in your OpMode loop.
     */
    public void update() {
        updateFlight();
        updateFireBreath();
        telemetry.update();
    }
    
    /**
     * Stops all dragon motors and servos.
     * Use this for emergency stop or shutdown.
     */
    public void stopAll() {
        stopAllMotors();
        headServo.setPosition(0.5); // Return head to neutral
        fireBreathServo.setPosition(0.0); // Close fire breath valve
        isFlying = false;
        isBreathingFire = false;
        currentAltitude = 0.0;
    }
    
    /**
     * Internal method to stop all motors.
     */
    private void stopAllMotors() {
        leftWingMotor.set(0.0);
        rightWingMotor.set(0.0);
        verticalThrustMotor.set(0.0);
    }
}

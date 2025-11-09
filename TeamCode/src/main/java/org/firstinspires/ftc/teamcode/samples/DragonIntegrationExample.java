package org.firstinspires.ftc.teamcode.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Dragon;
import org.firstinspires.ftc.teamcode.Robot;

/**
 * DragonIntegrationExample - Shows how to integrate Dragon with the existing Robot class.
 * 
 * This example demonstrates using both the standard robot and dragon capabilities
 * in a single OpMode. This is useful for competitions where you want dragon features
 * alongside your regular robot control.
 * 
 * Driver 1 (gamepad1): Controls the robot drivetrain
 * Driver 2 (gamepad2): Controls the dragon
 * 
 * This follows the pattern established in CompBotBasic.java
 */
@TeleOp(name = "Robot + Dragon Integration", group = "Samples")
public class DragonIntegrationExample extends OpMode {
    
    // Robot components
    private Robot robot;
    private GamepadEx driver1;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    
    // Dragon components
    private Dragon dragon;
    private GamepadEx driver2;
    private ToggleButtonReader toggleDragonFlight;
    
    // Command scheduler
    private CommandScheduler commandScheduler;
    
    // Configuration
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;
    
    @Override
    public void init() {
        // Initialize robot (standard setup from CompBotBasic)
        driver1 = new GamepadEx(gamepad1);
        robot = new Robot(hardwareMap, telemetry);
        
        // Initialize dragon
        driver2 = new GamepadEx(gamepad2);
        dragon = new Dragon(hardwareMap, telemetry);
        
        // Initialize command scheduler
        commandScheduler = CommandScheduler.getInstance();
        
        // Driver 1 controls (robot)
        toggleDriveSlow = new ToggleButtonReader(
            driver1.getGamepadButton(GamepadKeys.Button.X)
                .and(driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER))::get
        );
        
        toggleFieldCentric = new ToggleButtonReader(
            driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.X))::get
        );
        
        // Driver 2 controls (dragon)
        toggleDragonFlight = new ToggleButtonReader(
            driver2.getGamepadButton(GamepadKeys.Button.A)::get
        );
        
        telemetry.addData("Status", "Robot + Dragon Initialized");
        telemetry.addLine();
        telemetry.addLine("Driver 1: Robot Control");
        telemetry.addLine("Driver 2: Dragon Control");
        telemetry.update();
    }
    
    @Override
    public void loop() {
        // Update all inputs
        loopReadStuff();
        
        // === ROBOT CONTROL (Driver 1) ===
        double driveSpeedLimit = getDriveSpeedLimit();
        Robot.DriveState driveState = getDriveState();
        robot.drive(driveState, driver1, driveSpeedLimit);
        
        // Robot mechanisms
        if (driver1.getButton(GamepadKeys.Button.Y)) {
            robot.runIntakeLift(1.0);
            robot.runGrabber();
        } else {
            robot.runIntakeLift(0.0);
            robot.stopGrabber();
        }
        
        // === DRAGON CONTROL (Driver 2) ===
        handleDragonFlight();
        handleDragonFireBreath();
        handleDragonTargeting();
        
        // Update dragon systems
        dragon.update();
        
        // Display telemetry
        displayTelemetry(driveState);
    }
    
    @Override
    public void stop() {
        // Safe shutdown
        dragon.stopAll();
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
    
    /**
     * Handles dragon flight control (Driver 2 A button).
     */
    private void handleDragonFlight() {
        if (toggleDragonFlight.getState()) {
            if (!dragon.isFlying()) {
                dragon.takeOff();
            }
        } else {
            if (dragon.isFlying()) {
                dragon.land();
            }
        }
    }
    
    /**
     * Handles dragon fire breathing (Driver 2 B button).
     */
    private void handleDragonFireBreath() {
        if (driver2.getButton(GamepadKeys.Button.B)) {
            dragon.breatheFire();
        }
    }
    
    /**
     * Handles dragon targeting with Driver 2 left stick.
     */
    private void handleDragonTargeting() {
        double targetX = driver2.getLeftX() * 48.0;
        double targetY = driver2.getLeftY() * 48.0;
        
        if (Math.abs(targetX) > 0.1 || Math.abs(targetY) > 0.1) {
            dragon.targetPeasant(targetX, targetY);
        }
    }
    
    /**
     * Gets drive speed limit based on toggle state.
     */
    private double getDriveSpeedLimit() {
        return toggleDriveSlow.getState() ? driveSlowSpeedLimit : driveFastSpeedLimit;
    }
    
    /**
     * Gets drive state based on toggle state.
     */
    private Robot.DriveState getDriveState() {
        return toggleFieldCentric.getState() 
            ? Robot.DriveState.FIELD_CENTRIC 
            : Robot.DriveState.ROBOT_CENTRIC;
    }
    
    /**
     * Reads all gamepad and toggle inputs.
     */
    private void loopReadStuff() {
        commandScheduler.run();
        driver1.readButtons();
        driver2.readButtons();
        toggleDriveSlow.readValue();
        toggleFieldCentric.readValue();
        toggleDragonFlight.readValue();
    }
    
    /**
     * Displays comprehensive telemetry for both robot and dragon.
     */
    private void displayTelemetry(Robot.DriveState driveState) {
        telemetry.addLine("=== ROBOT STATUS ===");
        telemetry.addData("Drive Mode", driveState.name());
        telemetry.addData("Speed Limit", "%.0f%%", getDriveSpeedLimit() * 100);
        
        telemetry.addLine();
        telemetry.addLine("=== DRAGON STATUS ===");
        telemetry.addData("Flight", dragon.isFlying() ? "ACTIVE" : "Grounded");
        telemetry.addData("Altitude", "%.1f inches", dragon.getAltitude());
        telemetry.addData("Fire Breath", dragon.isBreathingFire() ? "ACTIVE" : "Ready");
        
        if (dragon.getFireBreathCooldown() > 0) {
            telemetry.addData("Fire Cooldown", "%.1f sec", dragon.getFireBreathCooldown());
        }
        
        telemetry.addData("Target in Range", dragon.isTargetInRange() ? "YES" : "NO");
        
        telemetry.update();
    }
}

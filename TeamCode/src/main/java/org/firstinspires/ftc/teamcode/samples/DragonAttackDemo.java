package org.firstinspires.ftc.teamcode.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Dragon;

/**
 * DragonAttackDemo - Demonstrates dragon flight and fire-breathing in an autonomous sequence.
 * 
 * This sample shows a complete dragon attack scenario:
 * 1. Dragon takes off and reaches flight altitude
 * 2. Dragon targets multiple peasants in sequence
 * 3. Dragon breathes fire on each target
 * 4. Dragon lands safely after attack
 * 
 * This is an example of how to use the Dragon class in an autonomous OpMode.
 */
@Autonomous(name = "Dragon Attack Demo", group = "Samples")
@Disabled
public class DragonAttackDemo extends LinearOpMode {
    
    private Dragon dragon;
    private ElapsedTime runtime = new ElapsedTime();
    
    // Simulated peasant locations (in inches from dragon starting position)
    private static final double[][] PEASANT_LOCATIONS = {
        {24.0, 12.0},   // Peasant 1: 24" forward, 12" right
        {36.0, 0.0},    // Peasant 2: 36" straight ahead
        {30.0, -18.0},  // Peasant 3: 30" forward, 18" left
        {48.0, 6.0}     // Peasant 4: 48" forward, 6" right (at max range)
    };
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing Dragon...");
        telemetry.update();
        
        // Initialize dragon
        dragon = new Dragon(hardwareMap, telemetry);
        
        telemetry.addData("Status", "Dragon Ready");
        telemetry.addData("Peasants to Burn", PEASANT_LOCATIONS.length);
        telemetry.addLine();
        telemetry.addLine("Press START to begin dragon attack!");
        telemetry.update();
        
        waitForStart();
        runtime.reset();
        
        if (opModeIsActive()) {
            performDragonAttack();
        }
        
        // Cleanup
        dragon.stopAll();
    }
    
    /**
     * Executes the complete dragon attack sequence.
     */
    private void performDragonAttack() {
        telemetry.addLine("=== DRAGON ATTACK SEQUENCE ===");
        telemetry.update();
        
        // Phase 1: Takeoff
        takeoffSequence();
        
        // Phase 2: Attack each peasant
        attackPeasants();
        
        // Phase 3: Victory lap
        victoryLap();
        
        // Phase 4: Landing
        landingSequence();
        
        telemetry.addLine();
        telemetry.addData("Mission", "COMPLETE!");
        telemetry.addData("Peasants Burned", PEASANT_LOCATIONS.length);
        telemetry.update();
    }
    
    /**
     * Phase 1: Dragon takeoff sequence
     */
    private void takeoffSequence() {
        telemetry.addLine();
        telemetry.addLine("Phase 1: TAKEOFF");
        telemetry.update();
        
        dragon.takeOff();
        
        // Wait for dragon to reach flight altitude
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 5.0) {
            dragon.update();
            
            telemetry.addData("Altitude", "%.1f inches", dragon.getAltitude());
            telemetry.addData("Status", "Ascending...");
            telemetry.update();
            
            sleep(50);
        }
        
        telemetry.addLine("Takeoff complete - Flight altitude reached!");
        telemetry.update();
        sleep(1000);
    }
    
    /**
     * Phase 2: Attack all peasants in sequence
     */
    private void attackPeasants() {
        telemetry.addLine();
        telemetry.addLine("Phase 2: ATTACKING PEASANTS");
        telemetry.update();
        sleep(500);
        
        for (int i = 0; i < PEASANT_LOCATIONS.length && opModeIsActive(); i++) {
            attackSinglePeasant(i + 1, PEASANT_LOCATIONS[i][0], PEASANT_LOCATIONS[i][1]);
        }
    }
    
    /**
     * Attacks a single peasant at the specified location.
     */
    private void attackSinglePeasant(int peasantNumber, double x, double y) {
        telemetry.addLine();
        telemetry.addData("Target", "Peasant #%d", peasantNumber);
        telemetry.addData("Location", "(%.1f, %.1f)", x, y);
        telemetry.update();
        
        // Target the peasant
        dragon.targetPeasant(x, y);
        
        // Wait for head to aim
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 1.0) {
            dragon.update();
            telemetry.addData("Status", "Aiming...");
            telemetry.update();
            sleep(50);
        }
        
        // Check if in range
        if (!dragon.isTargetInRange()) {
            telemetry.addData("Status", "OUT OF RANGE - Skipping");
            telemetry.update();
            sleep(2000);
            return;
        }
        
        // Breathe fire!
        telemetry.addData("Status", "BURNING PEASANT!");
        telemetry.update();
        
        boolean fireActivated = dragon.breatheFire();
        
        if (fireActivated) {
            // Wait for fire breath to complete
            runtime.reset();
            while (opModeIsActive() && runtime.seconds() < 2.5) {
                dragon.update();
                
                if (dragon.isBreathingFire()) {
                    telemetry.addData("Status", "🔥 FIRE BREATHING 🔥");
                } else {
                    telemetry.addData("Status", "Fire breath complete");
                }
                telemetry.update();
                sleep(50);
            }
            
            telemetry.addData("Result", "Peasant #%d eliminated!", peasantNumber);
            telemetry.update();
            sleep(1000);
            
            // Wait for cooldown before next attack
            telemetry.addData("Status", "Recharging fire breath...");
            telemetry.update();
            
            runtime.reset();
            while (opModeIsActive() && dragon.getFireBreathCooldown() > 0.1) {
                dragon.update();
                telemetry.addData("Cooldown", "%.1f sec", dragon.getFireBreathCooldown());
                telemetry.update();
                sleep(100);
            }
        } else {
            telemetry.addData("Status", "Fire breath on cooldown - waiting...");
            telemetry.update();
            sleep(3000);
        }
    }
    
    /**
     * Phase 3: Victory lap in the sky
     */
    private void victoryLap() {
        telemetry.addLine();
        telemetry.addLine("Phase 3: VICTORY LAP");
        telemetry.update();
        
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 3.0) {
            dragon.update();
            
            // Circle around for dramatic effect
            double angle = runtime.seconds() * Math.PI; // Complete circle in 3 seconds
            double victoryX = 30.0 * Math.cos(angle);
            double victoryY = 30.0 * Math.sin(angle);
            dragon.targetPeasant(victoryX, victoryY);
            
            telemetry.addData("Status", "Celebrating victory!");
            telemetry.addData("Altitude", "%.1f inches", dragon.getAltitude());
            telemetry.update();
            
            sleep(50);
        }
    }
    
    /**
     * Phase 4: Landing sequence
     */
    private void landingSequence() {
        telemetry.addLine();
        telemetry.addLine("Phase 4: LANDING");
        telemetry.update();
        
        dragon.land();
        
        // Wait for dragon to land
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 5.0 && dragon.getAltitude() > 1.0) {
            dragon.update();
            
            telemetry.addData("Altitude", "%.1f inches", dragon.getAltitude());
            telemetry.addData("Status", "Descending...");
            telemetry.update();
            
            sleep(50);
        }
        
        telemetry.addLine("Landing complete - Dragon safely on ground");
        telemetry.update();
        sleep(1000);
    }
}

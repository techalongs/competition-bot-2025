package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Dragon;

/**
 * DragonOpMode - Example OpMode demonstrating dragon flight and fire-breathing.
 * 
 * Controls:
 * - A Button: Take off / Land
 * - B Button: Breathe fire
 * - Left Stick: Target peasants (X/Y coordinates)
 * - Right Stick X: Adjust target range
 * 
 * This OpMode shows how to integrate the Dragon class with SolversLib
 * and the competition bot framework.
 */
@TeleOp(name = "Dragon Control", group = "Dragon")
public class DragonOpMode extends OpMode {
    
    private Dragon dragon;
    private GamepadEx gamepad;
    private ToggleButtonReader toggleFlight;
    
    // Peasant simulation (for targeting demonstration)
    private double peasantX = 0.0;
    private double peasantY = 24.0; // 24 inches in front
    
    @Override
    public void init() {
        // Initialize dragon
        dragon = new Dragon(hardwareMap, telemetry);
        
        // Initialize gamepad with SolversLib
        gamepad = new GamepadEx(gamepad1);
        
        // Flight toggle: A button
        toggleFlight = new ToggleButtonReader(
            gamepad.getGamepadButton(GamepadKeys.Button.A)::get
        );
        
        telemetry.addData("Dragon OpMode", "Initialized");
        telemetry.addData("Status", "Ready to fly!");
        telemetry.addLine();
        telemetry.addLine("Controls:");
        telemetry.addLine("  A Button: Take off / Land");
        telemetry.addLine("  B Button: Breathe fire");
        telemetry.addLine("  Left Stick: Target peasants");
        telemetry.update();
    }
    
    @Override
    public void loop() {
        // Read gamepad inputs
        gamepad.readButtons();
        toggleFlight.readValue();
        
        // Handle flight control
        if (toggleFlight.getState()) {
            if (!dragon.isFlying()) {
                dragon.takeOff();
            }
        } else {
            if (dragon.isFlying()) {
                dragon.land();
            }
        }
        
        // Handle fire breathing
        if (gamepad.getButton(GamepadKeys.Button.B)) {
            dragon.breatheFire();
        }
        
        // Handle peasant targeting with left stick
        double targetX = gamepad.getLeftX() * 48.0; // Scale to realistic range
        double targetY = gamepad.getLeftY() * 48.0;
        
        if (Math.abs(targetX) > 0.1 || Math.abs(targetY) > 0.1) {
            peasantX = targetX;
            peasantY = targetY;
        }
        
        dragon.targetPeasant(peasantX, peasantY);
        
        // Update dragon systems
        dragon.update();
        
        // Display status information
        displayStatus();
        
        // Display controls reminder
        displayControls();
    }
    
    @Override
    public void stop() {
        // Ensure dragon is safely stopped
        dragon.stopAll();
        telemetry.addData("Dragon OpMode", "Stopped");
        telemetry.update();
    }
    
    /**
     * Displays current dragon status on telemetry.
     */
    private void displayStatus() {
        telemetry.addLine("=== DRAGON STATUS ===");
        telemetry.addData("Flight Active", dragon.isFlying() ? "YES" : "NO");
        telemetry.addData("Current Altitude", "%.1f inches", dragon.getAltitude());
        telemetry.addData("Fire Breathing", dragon.isBreathingFire() ? "ACTIVE" : "Ready");
        
        if (!dragon.isBreathingFire() && dragon.getFireBreathCooldown() > 0) {
            telemetry.addData("Fire Cooldown", "%.1f sec", dragon.getFireBreathCooldown());
        }
        
        telemetry.addLine();
        telemetry.addLine("=== TARGET INFO ===");
        telemetry.addData("Peasant Location", "(%.1f, %.1f)", peasantX, peasantY);
        telemetry.addData("In Fire Range", dragon.isTargetInRange() ? "YES - BURN!" : "NO - Too far");
    }
    
    /**
     * Displays control instructions on telemetry.
     */
    private void displayControls() {
        telemetry.addLine();
        telemetry.addLine("=== CONTROLS ===");
        telemetry.addData("A", toggleFlight.getState() ? "Fly Mode ACTIVE" : "Fly Mode OFF");
        telemetry.addData("B", "Fire Breath Attack");
        telemetry.addData("Left Stick", "Target Peasants");
    }
}

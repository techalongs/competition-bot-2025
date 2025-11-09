package org.firstinspires.ftc.teamcode.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Dragon;

/**
 * DragonTest - Comprehensive test program for dragon functionality.
 * 
 * This test runs through all dragon features to verify proper operation:
 * - Flight mechanics test
 * - Fire-breathing test
 * - Targeting test
 * - Cooldown test
 * - Emergency stop test
 * 
 * Run this test after hardware configuration to ensure everything works correctly.
 */
@TeleOp(name = "Dragon System Test", group = "Tests")
public class DragonTest extends LinearOpMode {
    
    private Dragon dragon;
    private ElapsedTime runtime = new ElapsedTime();
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing Dragon Test...");
        telemetry.update();
        
        // Initialize dragon
        try {
            dragon = new Dragon(hardwareMap, telemetry);
            telemetry.addData("Status", "Dragon Initialized Successfully");
        } catch (Exception e) {
            telemetry.addData("ERROR", "Failed to initialize dragon");
            telemetry.addData("Exception", e.getMessage());
            telemetry.update();
            while (!isStopRequested()) {
                idle();
            }
            return;
        }
        
        telemetry.addLine();
        telemetry.addLine("Dragon Test Suite Ready");
        telemetry.addLine("Press START to begin tests");
        telemetry.update();
        
        waitForStart();
        runtime.reset();
        
        if (opModeIsActive()) {
            runTestSuite();
        }
        
        // Cleanup
        dragon.stopAll();
        telemetry.addData("Status", "Test Complete");
        telemetry.update();
    }
    
    /**
     * Runs the complete test suite.
     */
    private void runTestSuite() {
        telemetry.addLine("=== DRAGON SYSTEM TEST SUITE ===");
        telemetry.update();
        sleep(1000);
        
        // Test 1: Basic initialization
        if (!test1Initialization()) return;
        
        // Test 2: Flight mechanics
        if (!test2FlightMechanics()) return;
        
        // Test 3: Fire breathing
        if (!test3FireBreathing()) return;
        
        // Test 4: Targeting system
        if (!test4TargetingSystem()) return;
        
        // Test 5: Cooldown mechanism
        if (!test5CooldownMechanism()) return;
        
        // Test 6: Emergency stop
        if (!test6EmergencyStop()) return;
        
        // All tests passed
        telemetry.addLine();
        telemetry.addLine("=== ALL TESTS PASSED ===");
        telemetry.addData("Total Test Time", "%.1f seconds", runtime.seconds());
        telemetry.update();
        sleep(3000);
    }
    
    /**
     * Test 1: Verify dragon initialization
     */
    private boolean test1Initialization() {
        telemetry.addLine();
        telemetry.addLine("Test 1: Initialization Check");
        telemetry.update();
        sleep(500);
        
        try {
            // Verify dragon is not flying initially
            if (dragon.isFlying()) {
                testFailed("Dragon should not be flying initially");
                return false;
            }
            
            // Verify altitude is zero
            if (dragon.getAltitude() != 0.0) {
                testFailed("Initial altitude should be zero");
                return false;
            }
            
            // Verify not breathing fire
            if (dragon.isBreathingFire()) {
                testFailed("Dragon should not be breathing fire initially");
                return false;
            }
            
            testPassed("Initialization check");
            return true;
            
        } catch (Exception e) {
            testFailed("Initialization check - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 2: Flight mechanics
     */
    private boolean test2FlightMechanics() {
        telemetry.addLine();
        telemetry.addLine("Test 2: Flight Mechanics");
        telemetry.update();
        sleep(500);
        
        try {
            // Test takeoff
            dragon.takeOff();
            
            if (!dragon.isFlying()) {
                testFailed("Dragon should be flying after takeOff()");
                return false;
            }
            
            // Run flight for 3 seconds
            runtime.reset();
            double maxAltitude = 0.0;
            while (opModeIsActive() && runtime.seconds() < 3.0) {
                dragon.update();
                maxAltitude = Math.max(maxAltitude, dragon.getAltitude());
                
                telemetry.addData("Flight Time", "%.1f sec", runtime.seconds());
                telemetry.addData("Current Altitude", "%.1f inches", dragon.getAltitude());
                telemetry.addData("Max Altitude", "%.1f inches", maxAltitude);
                telemetry.update();
                
                sleep(50);
            }
            
            // Verify altitude increased
            if (maxAltitude < 10.0) {
                testFailed("Dragon should reach at least 10 inches altitude");
                return false;
            }
            
            // Test landing
            dragon.land();
            sleep(100);
            
            if (dragon.isFlying()) {
                testFailed("Dragon should not be flying after land()");
                return false;
            }
            
            testPassed("Flight mechanics");
            return true;
            
        } catch (Exception e) {
            testFailed("Flight mechanics - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 3: Fire breathing
     */
    private boolean test3FireBreathing() {
        telemetry.addLine();
        telemetry.addLine("Test 3: Fire Breathing");
        telemetry.update();
        sleep(500);
        
        try {
            // Activate fire breath
            boolean activated = dragon.breatheFire();
            
            if (!activated) {
                testFailed("Fire breath should activate on first call");
                return false;
            }
            
            // Update for fire breath duration
            runtime.reset();
            boolean wasBreathingFire = false;
            while (opModeIsActive() && runtime.seconds() < 3.0) {
                dragon.update();
                
                if (dragon.isBreathingFire()) {
                    wasBreathingFire = true;
                }
                
                telemetry.addData("Fire Breath Active", dragon.isBreathingFire());
                telemetry.addData("Time", "%.1f sec", runtime.seconds());
                telemetry.update();
                
                sleep(50);
            }
            
            if (!wasBreathingFire) {
                testFailed("Dragon should have breathed fire");
                return false;
            }
            
            if (dragon.isBreathingFire()) {
                testFailed("Fire breath should have completed by now");
                return false;
            }
            
            testPassed("Fire breathing");
            return true;
            
        } catch (Exception e) {
            testFailed("Fire breathing - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 4: Targeting system
     */
    private boolean test4TargetingSystem() {
        telemetry.addLine();
        telemetry.addLine("Test 4: Targeting System");
        telemetry.update();
        sleep(500);
        
        try {
            // Test close target (should be in range)
            dragon.targetPeasant(24.0, 12.0);
            
            if (!dragon.isTargetInRange()) {
                testFailed("Target at (24, 12) should be in range");
                return false;
            }
            
            // Test far target (should be out of range)
            dragon.targetPeasant(100.0, 100.0);
            
            if (dragon.isTargetInRange()) {
                testFailed("Target at (100, 100) should be out of range");
                return false;
            }
            
            // Test multiple targets
            double[][] testTargets = {
                {10.0, 10.0},
                {30.0, 0.0},
                {0.0, 40.0},
                {-20.0, 20.0}
            };
            
            for (double[] target : testTargets) {
                dragon.targetPeasant(target[0], target[1]);
                dragon.update();
                
                telemetry.addData("Target", "(%.1f, %.1f)", target[0], target[1]);
                telemetry.addData("In Range", dragon.isTargetInRange());
                telemetry.update();
                
                sleep(500);
            }
            
            testPassed("Targeting system");
            return true;
            
        } catch (Exception e) {
            testFailed("Targeting system - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 5: Cooldown mechanism
     */
    private boolean test5CooldownMechanism() {
        telemetry.addLine();
        telemetry.addLine("Test 5: Cooldown Mechanism");
        telemetry.update();
        sleep(500);
        
        try {
            // First breath should work
            boolean firstBreath = dragon.breatheFire();
            if (!firstBreath) {
                testFailed("First fire breath should activate");
                return false;
            }
            
            // Wait for fire to complete
            sleep(2500);
            dragon.update();
            
            // Immediate second breath should fail (cooldown)
            boolean secondBreath = dragon.breatheFire();
            if (secondBreath) {
                testFailed("Second fire breath should be on cooldown");
                return false;
            }
            
            // Wait for cooldown
            telemetry.addData("Status", "Waiting for cooldown...");
            telemetry.update();
            
            runtime.reset();
            while (opModeIsActive() && dragon.getFireBreathCooldown() > 0.1) {
                dragon.update();
                
                telemetry.addData("Cooldown Remaining", "%.1f sec", dragon.getFireBreathCooldown());
                telemetry.update();
                
                sleep(100);
            }
            
            // Third breath after cooldown should work
            boolean thirdBreath = dragon.breatheFire();
            if (!thirdBreath) {
                testFailed("Fire breath should work after cooldown");
                return false;
            }
            
            sleep(2500);
            dragon.update();
            
            testPassed("Cooldown mechanism");
            return true;
            
        } catch (Exception e) {
            testFailed("Cooldown mechanism - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Test 6: Emergency stop
     */
    private boolean test6EmergencyStop() {
        telemetry.addLine();
        telemetry.addLine("Test 6: Emergency Stop");
        telemetry.update();
        sleep(500);
        
        try {
            // Start flight and fire
            dragon.takeOff();
            dragon.breatheFire();
            
            // Let it run briefly
            runtime.reset();
            while (opModeIsActive() && runtime.seconds() < 1.0) {
                dragon.update();
                sleep(50);
            }
            
            // Emergency stop
            dragon.stopAll();
            
            // Verify everything stopped
            if (dragon.isFlying()) {
                testFailed("Dragon should not be flying after stopAll()");
                return false;
            }
            
            if (dragon.isBreathingFire()) {
                testFailed("Dragon should not be breathing fire after stopAll()");
                return false;
            }
            
            testPassed("Emergency stop");
            return true;
            
        } catch (Exception e) {
            testFailed("Emergency stop - Exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to report test passed.
     */
    private void testPassed(String testName) {
        telemetry.addData(testName, "✓ PASSED");
        telemetry.update();
        sleep(1000);
    }
    
    /**
     * Helper method to report test failed.
     */
    private void testFailed(String reason) {
        telemetry.addData("TEST FAILED", reason);
        telemetry.update();
        sleep(5000);
    }
}

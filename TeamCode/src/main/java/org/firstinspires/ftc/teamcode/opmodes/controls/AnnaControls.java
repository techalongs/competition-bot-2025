package org.firstinspires.ftc.teamcode.opmodes.controls;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public class AnnaControls implements GamepadControls {

    private final GamepadEx driver1;
    private final GamepadEx driver2;
    private final Robot robot;
    private final ToggleButtonReader toggleDriveSlow;
    private final ToggleButtonReader toggleFieldCentric;
    private final Trigger launchAllTrigger;
    private final Launcher.Power[] launcherPowers = new Launcher.Power[]{Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};
    private final int[][] gamepadColors = new int[][]{{255, 0, 0}, {0, 0, 255}, {0, 255, 0}};
    private Launcher.Power launcherPower = Launcher.Power.SHORT;

    public AnnaControls(GamepadEx driver1, GamepadEx driver2, Robot robot) {
        this.driver1 = driver1;
        this.driver2 = driver2;
        this.robot = robot;

        // With multi button toggles there is an enter before the .and(...)
        // Drive Slow Toggle = Options + Left Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER))::get);

        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.SQUARE))::get);

        // Launcher Power controls = (Short = Square) (Mid = Triangle) (Long = Circle)
        // Launch Power: Short
        driver2.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(
                        new InstantCommand(() -> {
                            int index = 2;
                            launcherPower = launcherPowers[index];
                            driver2.gamepad.setLedColor(
                                    gamepadColors[index][0],
                                    gamepadColors[index][1],
                                    gamepadColors[index][2],
                                    5000);
                        })
                );

        // Launch Power: Mid
        driver2.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(
                        new InstantCommand(() -> {
                            int index = 1;
                            launcherPower = launcherPowers[index];
                            driver2.gamepad.setLedColor(
                                    gamepadColors[index][0],
                                    gamepadColors[index][1],
                                    gamepadColors[index][2],
                                    5000);
                        })
                );

        // Launch Power: Long
        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(
                        new InstantCommand(() -> {
                            int index = 0;
                            launcherPower = launcherPowers[index];
                            driver2.gamepad.setLedColor(
                                    gamepadColors[index][0],
                                    gamepadColors[index][1],
                                    gamepadColors[index][2],
                                    5000);
                        })
                );

        // Intake: Right bumper = run the intake, Left bumper = run the intake in reverse
        driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new ConditionalCommand(
                        robot.stopIntake(),
                        robot.runIntake(),
                        robot::intakeIsRunning
                ));

        driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new ConditionalCommand(
                        robot.stopIntake(),
                        robot.reverseIntake(),
                        robot::intakeIsRunning
                ));

        // Launch Purple
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.PURPLE, launcherPower), null));
        // Launch Green
        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.GREEN, launcherPower), null));
        // Launch All
        launchAllTrigger = new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(new DeferredCommand(() -> robot.launchAll(launcherPower), null));
        // Launch Left
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new DeferredCommand(() -> robot.launchLeft(launcherPower), null));
        // Launch Mid
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new DeferredCommand(() -> robot.launchMid(launcherPower), null));
        // Launch Right
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new DeferredCommand(() -> robot.launchRight(launcherPower), null));

        // TODO: Temporary
        // Launch Very Slowly
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new DeferredCommand(() -> robot.launchAllEventually(launcherPower), null));

    }

    @Override
    public ToggleButtonReader getDriveSlowToggleReader() {
        return toggleDriveSlow;
    }

    @Override
    public ToggleButtonReader getFieldCentricToggleReader() {
        return toggleFieldCentric;
    }

    @Override
    public Launcher.Power getLauncherPower() {
        return launcherPower;
    }

    public boolean isDefault() {
        return true;
    }

    @NonNull
    public String toString() {
        return "Anna";
    }

}

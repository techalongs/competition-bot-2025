package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.REVColorSensor;

import java.util.Arrays;

public class AnnaControls extends SubsystemBase implements GamepadControls{

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private boolean intakeState = false;
    private Launcher.Power launcherPower = Launcher.Power.SHORT;
    private final Launcher.Power[] launcherPowers =
            new Launcher.Power[]{Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};
    private int launcherState = 2;
    private final int[][] gamepadColors = new int[][]{{255, 0, 0}, {0, 0, 255}, {0, 255, 0}};


    public AnnaControls(GamepadEx driver1, GamepadEx driver2, Robot robot) {

        this.driver1 = driver1;
        this.driver2 = driver2;
        this.robot = robot;

        // With multi button toggles there is and enter before the .and
        //  Drive Slow Toggle = Options + Left Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER))::get);

        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.SQUARE))::get);


        // Launcher Power controls = (Short = Square) (Mid = Triangle) (Long = Circle)
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

        // Intake - Y
        driver1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new ConditionalCommand(
                        robot.runIntake(),
                        robot.stopIntake(),
                        () -> {
                            intakeState = !intakeState;
                            return intakeState;
                        }
                ));

        // Launchers - X
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.PURPLE, launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.GREEN, launcherPower), null));
        Trigger launchAllTrigger = new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(new DeferredCommand(() -> robot.launchAll(launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new DeferredCommand(() -> robot.launchLeft(launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new DeferredCommand(() -> robot.launchMid(launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new DeferredCommand(() -> robot.launchRight(launcherPower), null));
    }

    @Override
    public ToggleButtonReader getDriveSlowToggleReader() {
        return toggleDriveSlow;
    }

    @Override
    public ToggleButtonReader getFieldCentricToggleReader() {
        return toggleFieldCentric;
    }

    public boolean isDefault() {
        return false;
    }

    public String toString() {
        return "Anna's Controls are on!";
    }
}

package org.firstinspires.ftc.teamcode.opmodes.controls;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public class OtherControls implements GamepadControls {

    private final GamepadEx driver1;
    private final GamepadEx driver2;
    private final Robot robot;
    private final ToggleButtonReader toggleDriveSlow;
    private final ToggleButtonReader toggleFieldCentric;
    private final Launcher.Power[] launcherPowers = new Launcher.Power[]{Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};
    private final int[][] gamepadColors = new int[][]{{255, 0, 0}, {0, 0, 255}, {0, 255, 0}};

    private boolean intakeState = false;
    private Launcher.Power launcherPower = Launcher.Power.SHORT;
    private int launcherState = 2;

    public OtherControls(GamepadEx driver1, GamepadEx driver2, Robot robot) {

        this.driver1 = driver1;
        this.driver2 = driver2;
        this.robot = robot;

        // With multi button toggles there is and enter before the .and
        // Drive Slow Toggle = Options + Left Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER))::get);

        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.SQUARE))::get);

        // Launcher Power Toggle - Right Bumper
        driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(
                        new InstantCommand(() -> {
                            int index = ++launcherState % launcherPowers.length;
                            launcherPower = launcherPowers[index];
                            driver2.gamepad.setLedColor(
                                    gamepadColors[index][0],
                                    gamepadColors[index][1],
                                    gamepadColors[index][2],
                                    5000);
                        })
                );

        // Intake - Right bumper = run the intake, Left bumper = run the intake in reverse
        driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(new ConditionalCommand(
                        robot.runIntake(),
                        robot.stopIntake(),
                        () -> {
                            intakeState = !intakeState;
                            return intakeState;
                        }
                ));

        driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new ConditionalCommand(
                        robot.reverseIntake(),
                        robot.stopIntake(),
                        () -> {
                            intakeState = !intakeState;
                            return intakeState;
                        }
                ));

        // Launchers
        driver2.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.PURPLE, launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.GREEN, launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.CROSS)
                .whenPressed(new DeferredCommand(() -> robot.launchAll(launcherPower), null));

        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new DeferredCommand(() -> robot.launchLeft(launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new DeferredCommand(() -> robot.launchMid(launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new DeferredCommand(() -> robot.launchRight(launcherPower), null));

        // TODO: Temporary
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(new DeferredCommand(() -> robot.launchAllEventually(launcherPower), null));

        // Ascent Lifts - Dpad Up and Dpad Down
//        driver1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileHeld(robot.raiseLifts());
//        driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whileHeld(robot.lowerLifts());
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

    @NonNull
    @Override
    public String toString() {
        return "V1";
    }

}

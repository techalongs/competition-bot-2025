package org.firstinspires.ftc.teamcode.opmodes.controls;

import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.function.DoubleConsumer;

/**
 *
 */
public class ControlsV2 implements GamepadControls {

    /**
     *
     */
    public ControlsV2(GamepadEx driver1, GamepadEx driver2, Robot robot) {

        boolean singleController = (driver1 == driver2);

        // Drive Speed Limit
//        driver1.getGamepadButton(GamepadKeys.Button.TOUCHPAD).toggleWhenActive(
//                () -> RobotConfig.driveSpeedLimit = RobotConfig.driveSlowSpeedLimit,
//                () -> RobotConfig.driveSpeedLimit = RobotConfig.driveFastSpeedLimit);

        // Launcher Power controls = (Short = Square) (Mid = Triangle) (Long = Circle)
        // Launch Power: Short
        driver2.getGamepadButton(GamepadKeys.Button.SQUARE)
                .whenPressed(
                        new InstantCommand(() -> {
                            RobotConfig.setLauncherPower(Launcher.Power.SHORT);
                            setGamepadColors(driver2, Launcher.Power.SHORT);
                        })
                );

        // Launch Power: Mid
        driver2.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(
                        new InstantCommand(() -> {
                            RobotConfig.setLauncherPower(Launcher.Power.MID);
                            setGamepadColors(driver2, Launcher.Power.MID);
                        })
                );

        // Launch Power: Long
        driver2.getGamepadButton(GamepadKeys.Button.CIRCLE)
                .whenPressed(
                        new InstantCommand(() -> {
                            RobotConfig.setLauncherPower(Launcher.Power.LONG);
                            setGamepadColors(driver2, Launcher.Power.LONG);
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

        // "Plinko fork" - Dpad Down
        driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(robot.turnFork());

        // Launch All
//        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
//                .whenActive(new DeferredCommand(() -> robot.launchAll(() -> RobotConfig.launchRawPower), null));
        new Trigger(() -> driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(() -> robot.launchAll(() -> RobotConfig.launchRawPower));
        // Launch Left
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new DeferredCommand(() -> robot.launchLeft(RobotConfig.launchRawPower), null));
        // Launch Mid
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new DeferredCommand(() -> robot.launchMid(RobotConfig.launchRawPower), null));
        // Launch Right
        driver2.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new DeferredCommand(() -> robot.launchRight(RobotConfig.launchRawPower), null));
        if (!singleController) {
            // Launch Purple
            driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                    .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.PURPLE, RobotConfig.launcherPower), null));
            // Launch Green
            driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                    .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.GREEN, RobotConfig.launcherPower), null));
        }
    }

    public String toString() {
        return "V2";
    }

}

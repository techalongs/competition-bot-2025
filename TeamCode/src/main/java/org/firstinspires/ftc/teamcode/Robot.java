package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.LogitechCamera;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import lombok.Getter;

@Getter
public class Robot {
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Launcher leftLauncher;
    private final Launcher midLauncher;
    private final Launcher rightLauncher;
    private final List<Subsystem> allLaunchers = new ArrayList<>();
    private final LogitechCamera camera;
//    private final Lifter lifts;

    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        intake = new Intake(hardwareMap, "intakeLift", "intakeFork");
        leftLauncher = new Launcher(hardwareMap, "leftLauncher", "leftSensor", true);
        midLauncher = new Launcher(hardwareMap, "midLauncher", "midSensor", true);
        rightLauncher = new Launcher(hardwareMap, "rightLauncher", "rightSensor", false);
        allLaunchers.add(leftLauncher);
        allLaunchers.add(midLauncher);
        allLaunchers.add(rightLauncher);
        camera = new LogitechCamera(hardwareMap, "Webcam 1");
//        lifts = new Lifter(hardwareMap, "leftLift", "rightLift");
//        lifts.setDefaultCommand(new PerpetualCommand(new RunCommand(lifts::stop, lifts)));
    }

    public void drive(Drivetrain.DriveState state, GamepadEx gamepad, double limiter) {
        drivetrain.drive(state, gamepad, limiter);
    }

    public Command runIntake() {
        return new InstantCommand(intake::run);
    }

    public Command reverseIntake() {
        return new InstantCommand(intake::reverse);
    }

    public Command stopIntake() {
        return new SequentialCommandGroup(
                new InstantCommand(intake::stop),
                turnFork()
        );
    }

    public Command turnFork() {
        return new DeferredCommand(() -> {
            if (rightLauncher.getColor() == null && leftLauncher.getColor() == null)
                return null;

            if (midLauncher.getColor() != null && rightLauncher.getColor() == null)
                return new InstantCommand(intake::turnForkRight);

            if (midLauncher.getColor() != null && leftLauncher.getColor() == null)
                return new InstantCommand(intake::turnForkLeft);

            return null;
        }, null);
    }

    public Command testFork() {
        return new InstantCommand(intake::turnForkRight);
    }

    private Command launch(Launcher launcher, Launcher.Power power) {
        return launch(launcher, power::power);
    }

    private Command launch(Launcher launcher, double power) {
        return launch(launcher, () -> power);
    }

    private Command launch(Launcher launcher, DoubleSupplier power) {
        final Command cmd =  new SequentialCommandGroup(
                new InstantCommand(launcher::reload),
                new SleepCommand(RobotConfig.sleepBeforeLaunch),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(RobotConfig.sleepAfterLaunch),
                new InstantCommand(launcher::stopLauncher)
        );
        // TODO: We can remove this if we find there's no reason to sleep before reload.
        //       This shouldn't be a performance drag.
        if (RobotConfig.sleepBeforeReload > 0) {
            return cmd.beforeStarting(new SleepCommand(RobotConfig.sleepBeforeReload));
        }
        return cmd;
    }

    private Command launchWithDelay(Launcher launcher, DoubleSupplier power, int delayMs) {
        final Command cmd = new SequentialCommandGroup(
                new SleepCommand(delayMs),
                new InstantCommand(launcher::reload),
                new SleepCommand(RobotConfig.sleepBeforeLaunch),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(RobotConfig.sleepAfterLaunch),
                new InstantCommand(launcher::stopLauncher)
        );
        return cmd;
    }

    public Command launchColor(Launcher.Color color, Launcher.Power power) {
        return launchColor(color, power::power);
    }

    public Command launchColor(Launcher.Color color, DoubleSupplier power) {
        return new DeferredCommand(() -> {
            if (leftLauncher.getColor() == color) return launch(leftLauncher, power);
            if (midLauncher.getColor() == color) return launch(midLauncher, power);
            if (rightLauncher.getColor() == color) return launch(rightLauncher, power);
            return new InstantCommand();
        }, allLaunchers);
    }

    public Launcher.Color[] getLauncherColors() {
        return new Launcher.Color[] {leftLauncher.getColor(), midLauncher.getColor(), rightLauncher.getColor()};
    }

    public Command launchLeft(DoubleSupplier power) {
        return this.launch(leftLauncher, power);
    }

    public Command launchLeft(double power) {
        return this.launch(leftLauncher, power);
    }

    public Command launchLeft(Launcher.Power power) {
        return this.launch(leftLauncher, power);
    }

    public Command launchMid(DoubleSupplier power) {
        return this.launch(midLauncher, power);
    }

    public Command launchMid(double power) {
        return this.launch(midLauncher, power);
    }

    public Command launchMid(Launcher.Power power) {
        return this.launch(midLauncher, power);
    }

    public Command launchRight(DoubleSupplier power) {
        return this.launch(rightLauncher, power);
    }

    public Command launchRight(double power) {
        return this.launch(rightLauncher, power);
    }

    public Command launchRight(Launcher.Power power) {
        return this.launch(rightLauncher, power);
    }

    public Command launchAll(DoubleSupplier power) {
        return launchAllSequential(power);
    }

    public Command launchAll(double power) {
        return launchAllSequential(() -> power);
    }

    public Command launchAll(Launcher.Power power) {
        return launchAllSequential(power::power);
    }

    public Command launchAllSequential(DoubleSupplier power) {
        return new SequentialCommandGroup(
                this.launch(leftLauncher, power),
                new SleepCommand(RobotConfig.sleepBetweenSequentialLaunches),
                this.launch(midLauncher, power),
                new SleepCommand(RobotConfig.sleepBetweenSequentialLaunches),
                this.launch(rightLauncher, power)
        );
    }

    public Command launchAllParallel(DoubleSupplier power) {
        return new ParallelCommandGroup(
                this.launch(leftLauncher, power),
                this.launchWithDelay(midLauncher, power,
                        RobotConfig.sleepBeforeSecondParallelLauncher),
                this.launchWithDelay(rightLauncher, power,
                        RobotConfig.sleepBeforeSecondParallelLauncher + RobotConfig.sleepBeforeThirdParallelLauncher)
        );
    }

    /**
     * Launch a variable number of launchers sequentially with the configured timing between launches.
     * This is for the case where you want to launch in an order different than Left-Mid-Right.
     * For example, in motif order.
     */
    public Command launchAllSequential(DoubleSupplier power, Launcher... launchers) {
        SequentialCommandGroup commands = new SequentialCommandGroup();
        commands.addCommands(this.launch(launchers[0], power));
        if (launchers.length > 1) {
            commands.addCommands(
                    new SleepCommand(RobotConfig.sleepBetweenSequentialLaunches),
                    this.launch(launchers[1], power)
            );
        }
        if (launchers.length > 2) {
            commands.addCommands(
                    new SleepCommand(RobotConfig.sleepBetweenSequentialLaunches),
                    this.launch(launchers[2], power)
            );
        }
        return commands;
    }

    /**
     * Launch a variable number of launchers in parallel with the configured timing between launches.
     * This is for the case where you want to launch in an order different than Left-Mid-Right.
     * For example, in motif order.
     */
    public Command launchAllParallel(DoubleSupplier power, Launcher... launchers) {
        ParallelCommandGroup commands = new ParallelCommandGroup();
        commands.addCommands(this.launch(launchers[0], power));
        if (launchers.length > 1) {
            commands.addCommands(
                    new SleepCommand(RobotConfig.sleepBeforeSecondParallelLauncher),
                    this.launch(launchers[1], power)
            );
        }
        if (launchers.length > 2) {
            commands.addCommands(
                    new SleepCommand(RobotConfig.sleepBeforeThirdParallelLauncher),
                    this.launch(launchers[2], power)
            );
        }
        return commands;
    }

    /**
     * Old launchAll for posterity.
     */
    public Command launchAll_Old(Launcher.Power power) {
        return launchAll_Old(power::power);
    }

    /**
     * Old launchAll for posterity.
     */
    public Command launchAll_Old(DoubleSupplier power) {
        return new ParallelCommandGroup(
                launch(leftLauncher, power),
                new SequentialCommandGroup(
                        new SleepCommand(50),
                        launch(midLauncher, power),
                        launch(rightLauncher, power)
                )
        );
    }

//    public Command raiseLifts() {
//        return new InstantCommand(lifts::raise, lifts);
//    }

//    public Command lowerLifts() {
//        return new InstantCommand(lifts::lower, lifts);
//    }

    public boolean intakeIsRunning() {
        return intake.isRunning();
    }

}

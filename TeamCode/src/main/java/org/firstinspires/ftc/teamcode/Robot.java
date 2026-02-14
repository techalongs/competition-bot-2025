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

//    public Command turnFork() {
//        return new DeferredCommand(() -> {
//            if (rightLauncher.getColor() == null && leftLauncher.getColor() == null)
//                return null;
//
//            if (midLauncher.getColor() != null && rightLauncher.getColor() == null)
//                return new InstantCommand(intake::turnForkRight);
//
//            if (midLauncher.getColor() != null && leftLauncher.getColor() == null)
//                return new InstantCommand(intake::turnForkLeft);
//
//            return null;
//        }, null);
//    }

    public Command turnFork() {
        return new SequentialCommandGroup(
                new InstantCommand(intake::turnForkRight),
                new SleepCommand(500),
                new InstantCommand(intake::turnForkLeft),
                new SleepCommand(500),
                new InstantCommand(intake::resetFork)
        );
    }

    private Command launch(Launcher launcher, DoubleSupplier power) {
        return new SequentialCommandGroup(
                new InstantCommand(launcher::reload),
                new SleepCommand(RobotConfig.sleepBeforeLaunch),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(RobotConfig.sleepAfterLaunch),
                new InstantCommand(launcher::stopLauncher)
        );
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

    public Command launchMid(DoubleSupplier power) {
        return this.launch(midLauncher, power);
    }

    public Command launchRight(DoubleSupplier power) {
        return this.launch(rightLauncher, power);
    }
    public Command launchAll(DoubleSupplier power) {
        return new ParallelCommandGroup(
                launch(leftLauncher, power),
                launch(midLauncher, power),
                launch(rightLauncher, power)
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

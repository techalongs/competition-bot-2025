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

public class Robot {
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Launcher leftLauncher;
    private final Launcher midLauncher;
    private final Launcher rightLauncher;
    private final LogitechCamera camera;
//    private final Lifter lifts;

    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        intake = new Intake(hardwareMap, "intakeLift");
        leftLauncher = new Launcher(hardwareMap, "leftLauncher", "leftSensor", true);
        midLauncher = new Launcher(hardwareMap, "midLauncher", "midSensor", true);
        rightLauncher = new Launcher(hardwareMap, "rightLauncher", "rightSensor", false);
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

    public Command stopIntake() {
        return new InstantCommand(intake::stop);
    }

    private Command launch(Launcher launcher, Launcher.Power power) {
        return new SequentialCommandGroup(
                new InstantCommand(launcher::reload),
                new SleepCommand(200),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(250),
                new InstantCommand(launcher::stopLauncher)
        );
    }

    public Command launchColor(Launcher.Color color, Launcher.Power power) {
        ArrayList<Subsystem> launchers = new ArrayList<>();
        launchers.add(leftLauncher);
        launchers.add(midLauncher);
        launchers.add(rightLauncher);

        return new DeferredCommand(() -> {
            if (leftLauncher.getColor() == color) return launch(leftLauncher, power);
            if (midLauncher.getColor() == color) return launch(midLauncher, power);
            if (rightLauncher.getColor() == color) return launch(rightLauncher, power);
            return new InstantCommand();
        }, launchers);
    }
    
    public Launcher.Color[] getLauncherColors() {
        return new Launcher.Color[] {leftLauncher.getColor(), midLauncher.getColor(), rightLauncher.getColor()};
    }

    public Command launchAll(Launcher.Power power) {
        return new ParallelCommandGroup(
                launch(leftLauncher, power),
                new SequentialCommandGroup(
                        new SleepCommand(50),
                        launch(midLauncher, power)
                ),
                new SequentialCommandGroup(
                        new SleepCommand(300),
                        launch(rightLauncher, power)
                )
        );
    }

    public Command launchLeft(Launcher.Power power) {
        return this.launch(leftLauncher, power);
    }

    public Command launchMid(Launcher.Power power) {
        return this.launch(midLauncher, power);
    }

    public Command launchRight(Launcher.Power power) {
        return this.launch(rightLauncher, power);
    }

//    public Command raiseLifts() {
//        return new InstantCommand(lifts::raise, lifts);
//    }

//    public Command lowerLifts() {
//        return new InstantCommand(lifts::lower, lifts);
//    }
}
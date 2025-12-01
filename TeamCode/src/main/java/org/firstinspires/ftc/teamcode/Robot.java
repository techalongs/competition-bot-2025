package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class Robot {
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Launcher leftLauncher;
    private final Launcher midLauncher;
    private final Launcher rightLauncher;
//    private final Lifter lifts;

    public enum DriveState {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    public Robot(HardwareMap hardwareMap) {
        drivetrain = new Drivetrain(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        intake = new Intake(hardwareMap, "intakeLift");
        leftLauncher = new Launcher(hardwareMap, "leftLauncher", "sensor1", true);
        midLauncher = new Launcher(hardwareMap, "midLauncher", "sensor2", true);
        rightLauncher = new Launcher(hardwareMap, "rightLauncher", "sensor3", true);
//        lifts = new Lifter(hardwareMap, "leftLift", "rightLift");
//        lifts.setDefaultCommand(new PerpetualCommand(new RunCommand(lifts::stop, lifts)));
    }

    public void drive(Robot.DriveState state, GamepadEx gamepad, double limiter) {
        drivetrain.drive(state, gamepad, limiter);
    }

    public Command runIntake() {
        return new InstantCommand(intake::run);
    }

    public Command stopIntake() {
        return new InstantCommand(intake::stop);
    }

    private Command launch(Launcher launcher) {
        return new SequentialCommandGroup(
                new InstantCommand(launcher::launch),
                new SleepCommand(150),
                new InstantCommand(launcher::reload),
                new SleepCommand(70),
                new InstantCommand(launcher::stopLauncher)
        );
    }

    public Command launchLeft() {
        return launch(leftLauncher);
    }

    public Command launchMid() {
        return launch(midLauncher);
    }

    public Command launchRight() {
        return launch(rightLauncher);
    }


//    public Command launchColor(Launcher.Color color) {
//        if (leftLauncher.getColor() == color) return new InstantCommand(leftLauncher::launch);
//        if (midLauncher.getColor() == color) return new InstantCommand(midLauncher::launch);
//        if (rightLauncher.getColor() == color) return new InstantCommand(rightLauncher::launch);
//        return new InstantCommand();
//    }

    public Command launchAll() {
        return new ParallelCommandGroup(
                launchLeft(),
                new SequentialCommandGroup(
                        new SleepCommand(100),
                        launchMid()
                ),
                new SequentialCommandGroup(
                        new SleepCommand(200),
                        launchRight()
                )
        );
    }

//    public Command raiseLifts() {
//        return new InstantCommand(lifts::raise, lifts);
//    }

//    public Command lowerLifts() {
//        return new InstantCommand(lifts::lower, lifts);
//    }
}
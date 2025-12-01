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

    public Command launchLeft() {
        return new InstantCommand(leftLauncher::launch);
    }

    public Command stopLeftLaunch() {
        return new InstantCommand(leftLauncher::stopLauncher);
    }

    public Command launchMid() {
        return new InstantCommand(midLauncher::launch);
    }

    public Command stopMidLaunch() {
        return new InstantCommand(midLauncher::stopLauncher);
    }

    public Command launchRight() {
        return new InstantCommand(rightLauncher::launch);
    }

    public Command stopRightLaunch() {
        return new InstantCommand(rightLauncher::stopLauncher);
    }

//    public Command launchColor(Launcher.Color color) {
//        if (leftLauncher.getColor() == color) return new InstantCommand(leftLauncher::launch);
//        if (midLauncher.getColor() == color) return new InstantCommand(midLauncher::launch);
//        if (rightLauncher.getColor() == color) return new InstantCommand(rightLauncher::launch);
//        return new InstantCommand();
//    }

    public Command launchAll() {
        return new SequentialCommandGroup(
                launchLeft(),
                new SleepCommand(500),
                launchMid(),
                stopLeftLaunch(),
                new SleepCommand(500),
                launchRight(),
                stopMidLaunch(),
                new SleepCommand(500),
                stopRightLaunch()
        );
    }

    public Command stopAllLaunch() {
        return new ParallelCommandGroup(stopLeftLaunch(), stopMidLaunch(), stopRightLaunch());
    }

//    public Command raiseLifts() {
//        return new InstantCommand(lifts::raise, lifts);
//    }

//    public Command lowerLifts() {
//        return new InstantCommand(lifts::lower, lifts);
//    }
}
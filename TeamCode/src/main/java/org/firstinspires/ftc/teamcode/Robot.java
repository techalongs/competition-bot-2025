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
import org.firstinspires.ftc.teamcode.util.SleepCommand;

import java.util.ArrayList;

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
                new InstantCommand(launcher::reloadSlow),
                new SleepCommand(300),
                new InstantCommand(launcher::launch),
                new SleepCommand(150),
                new InstantCommand(launcher::reload),
                new SleepCommand(25),
                new InstantCommand(launcher::stopLauncher)
        );
    }

    public Command launchColor(Launcher.Color color) {
        ArrayList<Subsystem> launchers = new ArrayList<>();
        launchers.add(leftLauncher);
        launchers.add(midLauncher);
        launchers.add(rightLauncher);

        return new DeferredCommand(() -> {
            if (leftLauncher.getColor() == color) return launch(leftLauncher);
            if (midLauncher.getColor() == color) return launch(midLauncher);
            if (rightLauncher.getColor() == color) return launch(rightLauncher);
            return new InstantCommand();
        }, launchers);
    }
    
    public Launcher.Color[] getLauncherColors() {
        return new Launcher.Color[] {leftLauncher.getColor(), midLauncher.getColor(), rightLauncher.getColor()};
    }

    public Command launchAll() {
        return new ParallelCommandGroup(
                launch(leftLauncher),
                new SequentialCommandGroup(
                        new SleepCommand(100),
                        launch(midLauncher)
                ),
                new SequentialCommandGroup(
                        new SleepCommand(200),
                        launch(rightLauncher)
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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Lifter;

public class Robot {
    private final Drivetrain drivetrain;
    private final Intake intake;
    private final Launcher backLauncher;
    private final Lifter lifts;
    private final Telemetry telemetry;

    public enum DriveState {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        drivetrain = new Drivetrain(hardwareMap, "frontLeft", "frontRight", "backLeft", "backRight");
        intake = new Intake(hardwareMap, "intakeLift");
        backLauncher = new Launcher(hardwareMap, "backLauncher", "pot3");
        lifts = new Lifter(hardwareMap, "leftLift", "rightLift");
        lifts.setDefaultCommand(new PerpetualCommand(new RunCommand(lifts::stop, lifts)));

        this.telemetry = telemetry;
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

    public Command launchBack() {
        return backLauncher.launch();
    }

    public Command raiseLifts() {
        return new InstantCommand(lifts::raise, lifts);
    }

    public Command lowerLifts() {
        return new InstantCommand(lifts::lower, lifts);
    }
}
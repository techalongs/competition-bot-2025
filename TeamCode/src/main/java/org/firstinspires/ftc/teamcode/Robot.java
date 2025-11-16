package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.Lifter;
import org.firstinspires.ftc.teamcode.util.NewIMU;

public class Robot {
    private final MecanumDrive drivetrain;
    private final Launcher backLauncher;
    private final Lifter lifts;
    private final Telemetry telemetry;
    private final NewIMU imu;
    private final MotorEx intakeLift;
    private final ServoEx lifter;
    private final CRServoEx grabber;
    private final double RAISE_LIFTER_POS = 0.65;
    private final double RESET_LIFTER_POS = 1;

    public enum DriveState {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        MotorEx frontLeft = new MotorEx(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312);
        MotorEx frontRight = new MotorEx(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312);
        MotorEx backLeft = new MotorEx(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312);
        MotorEx backRight = new MotorEx(hardwareMap, "backRight", Motor.GoBILDA.RPM_312);
        MotorEx leftLift = new MotorEx(hardwareMap, "leftLift");
        MotorEx rightLift = new MotorEx(hardwareMap, "rightLift");
        intakeLift = new MotorEx(hardwareMap, "intakeLift");
        lifter = new ServoEx(hardwareMap, "liftToIntake");
        grabber = new CRServoEx(hardwareMap, "intakeGrabber");

        frontLeft.setInverted(true);
        frontRight.setInverted(false);
        backLeft.setInverted(true);
        backRight.setInverted(false);
        leftLift.setInverted(false);
        rightLift.setInverted(true);
        intakeLift.setInverted(true);
        lifter.setInverted(false);
        grabber.setInverted(false);

        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intakeLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        frontLeft.stopAndResetEncoder();
        frontRight.stopAndResetEncoder();
        backLeft.stopAndResetEncoder();
        backRight.stopAndResetEncoder();
        leftLift.stopAndResetEncoder();
        rightLift.stopAndResetEncoder();
        intakeLift.stopAndResetEncoder();

        frontLeft.setRunMode(Motor.RunMode.VelocityControl);
        frontRight.setRunMode(Motor.RunMode.VelocityControl);
        backLeft.setRunMode(Motor.RunMode.VelocityControl);
        backRight.setRunMode(Motor.RunMode.VelocityControl);
        leftLift.setRunMode(Motor.RunMode.VelocityControl);
        rightLift.setRunMode(Motor.RunMode.VelocityControl);
        intakeLift.setRunMode(Motor.RunMode.RawPower);
        grabber.setRunMode(CRServoEx.RunMode.RawPower);

        drivetrain = new MecanumDrive(false, frontLeft, frontRight, backLeft, backRight);
        backLauncher = new Launcher(hardwareMap, "backLauncher", "pot3");
        lifts = new Lifter(hardwareMap, "leftLift", "rightLift");
        lifts.setDefaultCommand(new PerpetualCommand(new RunCommand(lifts::stop, lifts)));

        this.telemetry = telemetry;
        imu = new NewIMU(hardwareMap, "imu");
    }

    public void drive(DriveState state, GamepadEx gamepad, double limiter) {
        if (state == DriveState.ROBOT_CENTRIC) driveRobotCentric(gamepad, limiter);
        else if (state == DriveState.FIELD_CENTRIC) driveFieldCentric(gamepad, limiter);
        else throw new IllegalArgumentException("Not a valid Drive State");
    }

    public void runIntakeLift(double power) {
        intakeLift.set(power);
    }

    private void driveRobotCentric(GamepadEx gamepad, double limiter) {
        double strafeSpeed = gamepad.getLeftX() * limiter;
        double forwardSpeed = gamepad.getLeftY() * limiter;
        double turnSpeed = gamepad.getRightX() * limiter;

        drivetrain.driveRobotCentric(strafeSpeed, forwardSpeed, turnSpeed, false); // TODO - Try square inputs??
    }

    private void driveFieldCentric(GamepadEx gamepad, double limiter) {
        double strafeSpeed = gamepad.getLeftX() * limiter;
        double forwardSpeed = gamepad.getLeftY() * limiter;
        double turnSpeed = gamepad.getRightX() * limiter;

        drivetrain.driveFieldCentric(strafeSpeed, forwardSpeed, turnSpeed, imu.getRotation2d().getDegrees(), false);
    }

    public Command raiseLifts() {
        return new InstantCommand(lifts::raise, lifts);
    }

    public Command lowerLifts() {
        return new InstantCommand(lifts::lower, lifts);
    }

    public void raiseLifter() {
        lifter.set(RAISE_LIFTER_POS);
    }

    public void resetLifter() {
        lifter.set(RESET_LIFTER_POS);
    }

    public void turnOnGrabber() {
        grabber.set(1);
    }

    public void turnOffGrabber() {
        grabber.set(0);
    }

    public void launchBack() throws InterruptedException {
        backLauncher.launch();
    }
}
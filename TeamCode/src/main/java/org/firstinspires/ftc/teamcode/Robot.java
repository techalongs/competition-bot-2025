package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.BotLifters;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.NewIMU;

public class Robot {
    private final MecanumDrive drivetrain;
    private final Telemetry telemetry;
    private final NewIMU imu;
    private MotorEx intakeBubbler;

    private Servo servoLiftArtifactToLiftIntakeMotor;

    private final Intake intake;
    private final BotLifters botLifters;

    public enum DriveState {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        MotorEx motorFrontLeft = new MotorEx(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312);
        MotorEx motorFrontRight = new MotorEx(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312);
        MotorEx motorBackLeft = new MotorEx(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312);
        MotorEx motorBackRight = new MotorEx(hardwareMap, "backRight", Motor.GoBILDA.RPM_312);

        motorFrontLeft.setInverted(true);
        motorFrontRight.setInverted(false);
        motorBackLeft.setInverted(true);
        motorBackRight.setInverted(false);

        motorFrontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        motorFrontLeft.stopAndResetEncoder();
        motorFrontRight.stopAndResetEncoder();
        motorBackLeft.stopAndResetEncoder();
        motorBackRight.stopAndResetEncoder();

        motorFrontLeft.setRunMode(Motor.RunMode.VelocityControl);
        motorFrontRight.setRunMode(Motor.RunMode.VelocityControl);
        motorBackLeft.setRunMode(Motor.RunMode.VelocityControl);
        motorBackRight.setRunMode(Motor.RunMode.VelocityControl);

        intake = new Intake(hardwareMap, telemetry, "intakeLift");
        botLifters = new BotLifters(hardwareMap, telemetry);

        drivetrain = new MecanumDrive(false, motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);
        this.telemetry = telemetry;
        imu = new NewIMU(hardwareMap, "imu"); // TODO - Verify String id
    }

    public void drive(DriveState state, GamepadEx gamepad, double limiter) {
        if (state == DriveState.ROBOT_CENTRIC) driveRobotCentric(gamepad, limiter);
        else if (state == DriveState.FIELD_CENTRIC) driveFieldCentric(gamepad, limiter);
        else throw new IllegalArgumentException("Not a valid Drive State");
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

    public Intake getIntake() {
        return intake;
    }
    public BotLifters getBotLifters() {
        return botLifters;
    }

    //    private void runIntakeLift(double power) {
//        intakeLiftMotor.set(velocity);
//    }

}
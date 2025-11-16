package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.NewIMU;

public class Drivetrain extends SubsystemBase {
    private final MecanumDrive drivetrain;

    private final NewIMU imu;

    public enum DriveState {
        ROBOT_CENTRIC,
        FIELD_CENTRIC
    }

    public Drivetrain(HardwareMap hardwareMap, String flM, String frM, String blM, String brM) {
        MotorEx frontLeft = new MotorEx(hardwareMap, flM, Motor.GoBILDA.RPM_312);
        MotorEx frontRight = new MotorEx(hardwareMap, frM, Motor.GoBILDA.RPM_312);
        MotorEx backLeft = new MotorEx(hardwareMap, blM, Motor.GoBILDA.RPM_312);
        MotorEx backRight = new MotorEx(hardwareMap, brM, Motor.GoBILDA.RPM_312);

        frontLeft.setInverted(true);
        frontRight.setInverted(false);
        backLeft.setInverted(true);
        backRight.setInverted(false);

        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        frontLeft.stopAndResetEncoder();
        frontRight.stopAndResetEncoder();
        backLeft.stopAndResetEncoder();
        backRight.stopAndResetEncoder();

        frontLeft.setRunMode(Motor.RunMode.VelocityControl);
        frontRight.setRunMode(Motor.RunMode.VelocityControl);
        backLeft.setRunMode(Motor.RunMode.VelocityControl);
        backRight.setRunMode(Motor.RunMode.VelocityControl);

        drivetrain = new MecanumDrive(false, frontLeft, frontRight, backLeft, backRight);
        imu = new NewIMU(hardwareMap, "imu");
    }

    public void drive(Robot.DriveState state, GamepadEx gamepad, double limiter) {
        if (state == Robot.DriveState.ROBOT_CENTRIC) driveRobotCentric(gamepad, limiter);
        else if (state == Robot.DriveState.FIELD_CENTRIC) driveFieldCentric(gamepad, limiter);
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
}

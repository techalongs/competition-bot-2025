package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.geometry.Rotation2d;
import com.seattlesolvers.solverslib.hardware.GyroEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class NewIMU extends GyroEx {
    private final IMU imu;

    private double offset;
    private int multiplier;

    public NewIMU(HardwareMap hardwareMap, String id) {
        imu = hardwareMap.get(IMU.class, id);
        multiplier = 1;
    }

    @Override
    public void init() {
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );

        init(parameters);
    }

    public void init(IMU.Parameters parameters) {
        imu.initialize(parameters);
        offset = 0;
    }

    public void invertGyro() {
        multiplier *= -1;
    }

    @Override
    public double getHeading() {
        return getAbsoluteHeading() - offset;
    }

    @Override
    public double getAbsoluteHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) * multiplier;
    }

    @Override
    public double[] getAngles() {
        Orientation orientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES
        );

        return new double[] {orientation.firstAngle, orientation.secondAngle, orientation.thirdAngle};
    }

    @Override
    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    @Override
    public void reset() {
        offset += getHeading();
    }

    @Override
    public void disable() {
        imu.close();
    }

    @Override
    public String getDeviceType() {
        return "REV Control Hub IMU";
    }

    public IMU getRevIMU() {
        return imu;
    }

}

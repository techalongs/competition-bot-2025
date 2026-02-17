package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public final static String TAG = "techalongs";

    static {
        ForwardVelocityTuner.DISTANCE = 96;
        ForwardZeroPowerAccelerationTuner.VELOCITY = 55;
        LateralVelocityTuner.DISTANCE = 96;
        LateralZeroPowerAccelerationTuner.VELOCITY = 40;
    }

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(16.02)
            .forwardZeroPowerAcceleration(-34.397048567511156)
            .lateralZeroPowerAcceleration(-76.1239773798944)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.3, 0.0001, 0.01, 0.04))
            .headingPIDFCoefficients(new PIDFCoefficients(2.5, 0.5, 0.1, 0.05))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.03, 0.001, 0.0001, 0.6, 0.02))
            .centripetalScaling(0.0001);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-4.625)
            .strafePodX(0.1875)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRight")
            .rightRearMotorName("backRight")
            .leftRearMotorName("backLeft")
            .leftFrontMotorName("frontLeft")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(62.37995394008366)
            .yVelocity(48.80073511318898)
            ;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }


}

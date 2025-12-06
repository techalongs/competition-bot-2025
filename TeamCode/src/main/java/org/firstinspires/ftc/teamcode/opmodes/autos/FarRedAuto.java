package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Far Red Auto", group = "Autos")
public class FarRedAuto extends OpMode {
    private Robot robot;

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;

    private PathChain[] paths;
    private Pose[] poses;

    private void initPoses() {
        Pose startPose = new Pose(86.5, 9.5, Math.toRadians(0));
        Pose scorePose = new Pose(90, 90, Math.toRadians(45));
        Pose pickupPose = new Pose(129, 45, Math.toRadians(0));
        Pose endPose = new Pose(105, 15, Math.toRadians(0));
        poses = new Pose[] {startPose, scorePose, pickupPose, endPose};
    }

    private void buildPaths() {
        paths = new PathChain[4];

        // Score Preload
        paths[0] = follower.pathBuilder()
                .addPath(new BezierLine(poses[0], poses[1]))
                .setLinearHeadingInterpolation(poses[0].getHeading(), poses[1].getHeading())
                .build();

        // Grab far artifacts
        paths[1] = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                poses[1],
                                new Pose(95.000, 28.000),
                                new Pose(100.000, 35.500),
                                poses[2]
                        )
                )
                .setTangentHeadingInterpolation()
                .build();

        // Score artifacts
        paths[2] = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                poses[2],
                                new Pose(100.000, 35.500),
                                new Pose(95.000, 28.000),
                                poses[1]
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();

        // Park
        paths[3] = follower.pathBuilder()
                .addPath(new BezierLine(poses[1], poses[3]))
                .setLinearHeadingInterpolation(poses[1].getHeading(), poses[3].getHeading())
                .build();
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);

        initPoses();
        buildPaths();
        follower.setStartingPose(poses[0]);

        FarRedCommand auto = new FarRedCommand(robot, follower, paths);
        auto.schedule();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        // autonomousPathUpdate();
        CommandScheduler.getInstance().run();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
}

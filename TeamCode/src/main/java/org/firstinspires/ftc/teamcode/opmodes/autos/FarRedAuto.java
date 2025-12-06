package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Far Red Auto", group = "Autos")
public class FarRedAuto extends OpMode {
    private Robot robot;

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    private PathChain[] paths;
    private Pose[] poses;

    private void initPoses() {
        Pose startPose = new Pose(86.5, 9.5, Math.toRadians(270));
        Pose scorePose = new Pose(100, 99, Math.toRadians(45));
        Pose pickupPose = new Pose(129, 35, Math.toRadians(0));
        Pose endPose = new Pose(105, 15, Math.toRadians(180));
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

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths[pathState], true);
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    // TODO: Score Preload
                    follower.followPath(paths[pathState], true);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    // TODO: Grab Artifacts
                    follower.followPath(paths[pathState],true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    // TODO: Score Artifacts
                    follower.followPath(paths[pathState],true);
                    setPathState(4);
                }
                break;
            case 4:
                if(!follower.isBusy()) setPathState(-1);
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(poses[0]);
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
}

package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

@Autonomous(name = "Far Blue Auto", group = "Autos")
public class FarBlueAuto extends OpMode {
    private Robot robot;

    private Follower follower;
    private Timer pathTimer, opmodeTimer;
    private int pathState;

    private PathChain[] paths;
    private Pose[] poses;
    private final int DELAY = 0; // Milliseconds

    private void initPoses() {
        Pose startPose = new Pose(57, 9.5, Math.toRadians(0));
        Pose scorePose = new Pose(50, 93, Math.toRadians(135));
        Pose endPose = new Pose(40, 15, Math.toRadians(180));
        poses = new Pose[] {startPose, scorePose, endPose};
    }

    private void buildPaths() {
        paths = new PathChain[2];

        // Score Preload
        paths[0] = follower.pathBuilder()
                .addPath(new BezierLine(poses[0], poses[1]))
                .setLinearHeadingInterpolation(poses[0].getHeading(), poses[1].getHeading())
                .build();

        // Park
        paths[1] = follower.pathBuilder()
                .addPath(new BezierLine(poses[1], poses[2]))
                .setLinearHeadingInterpolation(poses[1].getHeading(), poses[2].getHeading())
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

        AutoCommand auto = new AutoCommand(robot, follower, paths, Launcher.Power.LONG);
        auto.schedule();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        CommandScheduler.getInstance().run();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }
}

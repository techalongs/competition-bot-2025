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
    private Timer opmodeTimer;

    private PathChain[] paths;

    @Override
    public void init() {
        robot = new Robot(hardwareMap);
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setStartingPose(BluePosition.LONG_START.pos);

        AutoCommand auto = new AutoCommand(robot, follower, paths, Launcher.Power.SHORT);
        auto.schedule();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
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
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    private void buildPaths() {
        paths = new PathChain[13];

//        paths[0] = getPath(BluePosition.LONG_START, BluePosition.LONG_SHOOT); // Score Preload
//        paths[1] = getPath(BluePosition.LONG_SHOOT, BluePosition.LONG_COLLECT_PREP); // Prep to collect
//        paths[2] = getPath(BluePosition.LONG_COLLECT_PREP, BluePosition.LONG_COLLECT); // Collect
//        paths[3] = getPath(BluePosition.LONG_COLLECT, BluePosition.LONG_SHOOT); // Score

        paths[0] = getPath(BluePosition.LONG_START, BluePosition.SHORT_SHOOT);
        paths[1] = getPath(BluePosition.SHORT_SHOOT, BluePosition.MID_COLLECT_PREP);
        paths[2] = getPath(BluePosition.MID_COLLECT_PREP, BluePosition.MID_COLLECT);
        paths[3] = getPath(BluePosition.MID_COLLECT, BluePosition.SHORT_SHOOT);
        paths[4] = getPath(BluePosition.SHORT_SHOOT, BluePosition.LONG_END);
    }

    private PathChain getPath(BluePosition point1, BluePosition point2) {
        return follower.pathBuilder()
                .addPath(new BezierLine(point1.pos, point2.pos))
                .setLinearHeadingInterpolation(point1.pos.getHeading(), point2.pos.getHeading())
                .build();
    }
}

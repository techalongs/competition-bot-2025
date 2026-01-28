package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

@Autonomous(name = "Close Red Auto", group = "Autos")
public class CloseRedAuto extends OpMode {
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
        follower.setStartingPose(RedPosition.SHORT_START.pos);

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
        paths = new PathChain[5];

        paths[0] = getPath(RedPosition.SHORT_START, RedPosition.SHORT_SHOOT); // Score Preload
        paths[1] = getPath(RedPosition.SHORT_SHOOT, RedPosition.SHORT_COLLECT_PREP); // Prep to collect
        paths[2] = getPath(RedPosition.SHORT_COLLECT_PREP, RedPosition.SHORT_COLLECT); // Collect
        paths[3] = getPath(RedPosition.SHORT_COLLECT, RedPosition.SHORT_SHOOT); // Score
        paths[4] = getPath(RedPosition.SHORT_SHOOT, RedPosition.SHORT_END); // Park
    }

    private PathChain getPath(RedPosition point1, RedPosition point2) {
        return follower.pathBuilder()
                .addPath(new BezierLine(point1.pos, point2.pos))
                .setLinearHeadingInterpolation(point1.pos.getHeading(), point2.pos.getHeading())
                .build();
    }
}

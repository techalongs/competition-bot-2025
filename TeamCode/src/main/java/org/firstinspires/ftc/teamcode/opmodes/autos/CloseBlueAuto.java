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

@Autonomous(name = "Close Blue Auto", group = "Autos")
public class CloseBlueAuto extends OpMode {

    private Follower follower;
    private Timer opmodeTimer;

    private PathChain[] paths;

    @Override
    public void init() {
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setStartingPose(BluePosition.SHORT_START.pos);

        AutoCommand auto = new AutoCommand(new Robot(hardwareMap), follower, paths, Launcher.Power.SHORT);
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

        paths[0] = getPath(BluePosition.SHORT_START, BluePosition.SHORT_SHOOT); // Score Preload
        paths[1] = getPath(BluePosition.SHORT_SHOOT, BluePosition.SHORT_COLLECT_PREP); // Prep to collect
        paths[2] = getPath(BluePosition.SHORT_COLLECT_PREP, BluePosition.SHORT_COLLECT); // Collect
        paths[3] = getPath(BluePosition.SHORT_COLLECT, BluePosition.SHORT_SHOOT); // Score
        paths[4] = getPath(BluePosition.SHORT_SHOOT, BluePosition.MID_COLLECT_PREP); // Prep to collect
        paths[5] = getPath(BluePosition.MID_COLLECT_PREP, BluePosition.MID_COLLECT); // Collect
        paths[6] = getPath(BluePosition.MID_COLLECT, BluePosition.DUMP_PREP); // Prep to dump
        paths[7] = getPath(BluePosition.DUMP_PREP, BluePosition.DUMP); // Dump
        paths[8] = getPath(BluePosition.DUMP, BluePosition.SHORT_SHOOT); // Score
        paths[9] = getPath(BluePosition.SHORT_SHOOT, BluePosition.LONG_COLLECT_PREP); // Prep to collect
        paths[10] = getPath(BluePosition.LONG_COLLECT_PREP, BluePosition.LONG_COLLECT); // Collect
        paths[11] = getPath(BluePosition.LONG_COLLECT, BluePosition.SHORT_SHOOT); // Score
        paths[12] = getPath(BluePosition.SHORT_SHOOT, BluePosition.SHORT_END); // Park
    }

    private PathChain getPath(BluePosition point1, BluePosition point2) {
        return follower.pathBuilder()
                .addPath(new BezierLine(point1.pos, point2.pos))
                .setLinearHeadingInterpolation(point1.pos.getHeading(), point2.pos.getHeading())
                .build();
    }
}

package org.firstinspires.ftc.teamcode.sandbox;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.autos.BluePosition;
import org.firstinspires.ftc.teamcode.opmodes.autos.RedPosition;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.TeamColor;

abstract public class MoveForwardAuto extends OpMode {

    protected TeamColor teamColor;
    protected Pose startPose;
    protected Pose endPose;
    protected Follower follower;
    protected PathChain forwardPath;
    protected Timer opModeTimer;
    protected double moveInches = 12;

    public MoveForwardAuto(Pose startPose, TeamColor teamColor) {
        this.startPose = startPose;
        this.teamColor = teamColor;
    }

    @Override
    public void init() {
        opModeTimer = new Timer();
        opModeTimer.resetTimer();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        endPose = new Pose(startPose.getX() + moveInches, startPose.getY(), startPose.getHeading());

        forwardPath = follower.pathBuilder()
                .addPath(new BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), endPose.getHeading())
                .build();

        telemetry.addLine("Ready to drive forward 300 mm");
        telemetry.update();
    }

    @Override
    public void start() {
        follower.followPath(forwardPath);
    }

    @Override
    public void loop() {
        follower.update();

        telemetry.addData("X (in)", "%.2f", follower.getPose().getX());
        telemetry.addData("Y (in)", "%.2f", follower.getPose().getY());
        telemetry.addData("Heading (°)", "%.1f", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    /**
     * Blue Version
     */
    @Autonomous(name = "• Move Fwd BLUE", group = "Sandbox")
    public static class MoveForwardBlueAuto extends MoveForwardAuto {
        public MoveForwardBlueAuto() {
            super(BluePosition.LONG_START.pos, TeamColor.BLUE);
        }
    }

    /**
     * Red Version
     */
    @Autonomous(name = "• Move Fwd RED", group = "Sandbox")
    public static class MoveForwardRedAuto extends MoveForwardAuto {
        public MoveForwardRedAuto() {
            super(RedPosition.LONG_START.pos, TeamColor.RED);
        }
    }

}

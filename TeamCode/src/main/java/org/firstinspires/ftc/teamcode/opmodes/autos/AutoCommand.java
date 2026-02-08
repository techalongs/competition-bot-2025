package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnCommand;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class AutoCommand extends SequentialCommandGroup {
    private final int DELAY = 0;
    public AutoCommand(Robot robot, Follower follower, PathChain[] paths, Launcher.Power power) {
        addCommands(
                new SequentialCommandGroup(
                        new SleepCommand(DELAY),
                        new FollowPathCommand(follower, paths[0], true),
                        robot.launchLeft(power), // Score preload
                        robot.launchMid(power),
                        robot.launchRight(power),
                        new FollowPathCommand(follower, paths[1], false),
                        robot.runIntake(), // Collect a row
                        new InstantCommand(() -> follower.setMaxPower(0.3)),
                        new FollowPathCommand(follower, paths[2], true),
                        new SleepCommand(2000),
                        robot.stopIntake(),
                        new InstantCommand(() -> follower.setMaxPower(1)),
                        new FollowPathCommand(follower, paths[3], false),
                        new SleepCommand(500),
                        new FollowPathCommand(follower, paths[4], true), // Dump
                        new SleepCommand(1000),
                        new FollowPathCommand(follower, paths[5], true),
                        robot.launchLeft(power), // Score
                        robot.launchMid(power),
                        robot.launchRight(power),
                        new FollowPathCommand(follower, paths[6], false),
                        robot.runIntake(), // Collect a row
                        new InstantCommand(() -> follower.setMaxPower(0.3)),
                        new FollowPathCommand(follower, paths[7], true),
                        new SleepCommand(2000),
                        robot.stopIntake(),
                        new InstantCommand(() -> follower.setMaxPower(1)),
                        new FollowPathCommand(follower, paths[8], true),
                        new SleepCommand(1000),
                        robot.launchLeft(power), // Score
                        robot.launchMid(power),
                        robot.launchRight(power),
                        new FollowPathCommand(follower, paths[9], false),
                        robot.runIntake(), // Collect a row
                        new InstantCommand(() -> follower.setMaxPower(0.3)),
                        new FollowPathCommand(follower, paths[10], true),
                        new SleepCommand(2000),
                        robot.stopIntake(),
                        new InstantCommand(() -> follower.setMaxPower(1)),
                        new FollowPathCommand(follower, paths[11], true),
                        new SleepCommand(1000),
                        robot.launchLeft(power), // Score
                        robot.launchMid(power),
                        robot.launchRight(power),
                        new FollowPathCommand(follower, paths[12], true)
                )
        );
    }
}

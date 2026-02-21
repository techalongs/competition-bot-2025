package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class AutoCommand extends SequentialCommandGroup {
    private final int DELAY = 0;
    private final double COLLECT_SPEED = 0.35;
    public AutoCommand(Robot robot, Follower follower, PathChain[] paths, Launcher.Power power) {
        addCommands(
                new SequentialCommandGroup(
                        new SleepCommand(DELAY),

                        // Score preload
                        new FollowPathCommand(follower, paths[0], true),
                        robot.launchAll(power.power),

                        // Collect first row
                        new FollowPathCommand(follower, paths[1], false),
                        robot.runIntake(),
                        new InstantCommand(() -> follower.setMaxPower(COLLECT_SPEED)),
                        new FollowPathCommand(follower, paths[2], true),
                        new InstantCommand(() -> follower.setMaxPower(1)),

                        // Score first row
                        new FollowPathCommand(follower, paths[3], true),
                        robot.stopIntake(),
                        new SleepCommand(500),
                        robot.launchAll(power.power),

                        // Collect second row
                        new FollowPathCommand(follower, paths[4], false),
                        robot.runIntake(),
                        new InstantCommand(() -> follower.setMaxPower(COLLECT_SPEED)),
                        new FollowPathCommand(follower, paths[5], true),
                        new InstantCommand(() -> follower.setMaxPower(1)),

//                        // Dump
//                        new FollowPathCommand(follower, paths[6], false),
//                        new SleepCommand(500),
//                        new FollowPathCommand(follower, paths[7], true),
//                        new SleepCommand(1000),

                        // Score second row
                        new FollowPathCommand(follower, paths[8], true),
                        robot.stopIntake(),
                        new SleepCommand(500),
                        robot.launchAll(power.power),

                        // Collect third row
                        new FollowPathCommand(follower, paths[9], false),
                        robot.runIntake(), // Collect a row
                        new InstantCommand(() -> follower.setMaxPower(COLLECT_SPEED)),
                        new FollowPathCommand(follower, paths[10], true),
                        new InstantCommand(() -> follower.setMaxPower(1)),

                        // Score third row
                        new FollowPathCommand(follower, paths[11], true),
                        robot.stopIntake(),
                        new SleepCommand(500),
                        robot.launchAll(power.power),

                        // Park
                        new FollowPathCommand(follower, paths[12], true)
                )
        );
    }
}

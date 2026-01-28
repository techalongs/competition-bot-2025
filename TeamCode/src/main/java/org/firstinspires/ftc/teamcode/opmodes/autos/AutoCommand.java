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
                        robot.launchAll(power), // Score preload
                        new FollowPathCommand(follower, paths[1], false),
                        robot.runIntake(), // Collect a row
                        new InstantCommand(() -> follower.setMaxPower(0.3)),
                        new FollowPathCommand(follower, paths[2], true),
                        new SleepCommand(1000),
                        robot.stopIntake(),
                        new InstantCommand(() -> follower.setMaxPower(1)),
                        new FollowPathCommand(follower, paths[3], true),
                        new SleepCommand(1000),
                        robot.launchAll(power), // Score
                        new FollowPathCommand(follower, paths[4], true)
                )
        );
    }
}

package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.Robot;

public class FarRedCommand extends SequentialCommandGroup {
    public FarRedCommand(Robot robot, Follower follower, PathChain[] paths) {
        addCommands(
                new FollowPathCommand(follower, paths[0], true),
                robot.launchAll(), // Score preload
                new FollowPathCommand(follower, paths[1], true), // TODO: Add intake to path
                new FollowPathCommand(follower, paths[2], true),
                robot.launchAll(), // Score artifacts
                new FollowPathCommand(follower, paths[3], true)
        );
    }
}

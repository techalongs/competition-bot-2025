package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class AutoCommand extends SequentialCommandGroup {
    public AutoCommand(Robot robot, Follower follower, PathChain[] paths, int delay) {
        addCommands(
                new SleepCommand(delay),
                new FollowPathCommand(follower, paths[0], true),
                robot.launchAll(Launcher.Power.LONG), // Score preload
                new FollowPathCommand(follower, paths[1], true)
        );
    }
}

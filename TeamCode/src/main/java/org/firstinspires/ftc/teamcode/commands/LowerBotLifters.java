package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;

import org.firstinspires.ftc.teamcode.subsystems.BotLifters;

import java.util.Collections;
import java.util.Set;

public class LowerBotLifters implements Command {


    private BotLifters botLifters;

    public LowerBotLifters(BotLifters botLifters) {
        this.botLifters = botLifters;
    }

    @Override
    public void initialize() {
    }

    public void execute(BotLifters botLifters) {
        botLifters.move2(-1.0);
    }

    public void end(boolean interrupted, BotLifters botLifters) {
        botLifters.move2(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.singleton(botLifters);
    }
}


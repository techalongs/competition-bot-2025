package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;

import org.firstinspires.ftc.teamcode.subsystems.BotLifters;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.Collections;
import java.util.Set;

public class StartBotLifters implements Command {


    private BotLifters botLifters;

    public StartBotLifters(BotLifters botLifters) {
        this.botLifters = botLifters;
    }

    @Override
    public void initialize() {
    }

    public void execute(BotLifters botLifters) {
        botLifters.move2(1.0);
    }

    public void end(boolean interrupted, BotLifters botLifters) {
        botLifters.move2(0.0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.singleton(botLifters);
    }
}


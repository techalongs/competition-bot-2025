package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.BotLifters;

public class RunBotLifter extends CommandBase {

    private BotLifters botLifters;
    private boolean liftBotUp;
    private boolean liftBotDown;

    public RunBotLifter(BotLifters botLifters, boolean liftBotUp) {
        this.liftBotUp = liftBotUp;
        this.botLifters = botLifters;
        this.addRequirements(botLifters);
    }

    @Override
    public void execute() {
       if (liftBotUp) {
//           botLifters.liftUp();
       } else if (liftBotDown) {
//           botLifters.lowerDown();
       }
    }

    @Override
    public void end(boolean interrupted) {
       if (botLifters.getCurrentPosition() > 0.0) {
//           botLifters.holdPosition();
       }
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}

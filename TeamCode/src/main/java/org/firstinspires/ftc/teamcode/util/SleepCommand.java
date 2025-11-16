package org.firstinspires.ftc.teamcode.util;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.util.Timing;

import java.util.concurrent.TimeUnit;

public class SleepCommand extends CommandBase {
    private Timing.Timer timer;
    public SleepCommand(int milliseconds) {
        timer = new Timing.Timer(milliseconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public boolean isFinished() {
        return timer.done();
    }
}

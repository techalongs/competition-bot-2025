package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.Collections;
import java.util.Set;

public class EndIntake implements Command {

    private Intake intake;

    public EndIntake(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void execute() {
        end();
    }

    public void end() {
        intake.stopBubbler();
        intake.stopGrabber();
        intake.lowerServoLifter();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return Collections.singleton(intake);
    }
}

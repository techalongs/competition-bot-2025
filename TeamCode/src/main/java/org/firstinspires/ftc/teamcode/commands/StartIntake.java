package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Subsystem;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.Collections;
import java.util.Set;

public class StartIntake implements Command {

    private Intake intake;

    public StartIntake(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
        intake.lowerLifter();
    }

    @Override
    public void execute() {
        intake.startBubbler();
        intake.startGrabber();
    }

    @Override
    public void end(boolean interrupted) {
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

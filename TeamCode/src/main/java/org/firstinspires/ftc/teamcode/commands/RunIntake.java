package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.function.Supplier;

public class RunIntake extends CommandBase {

    private Intake intake;
    private Supplier<Boolean> toggleIntake;

    public RunIntake(Intake intake, Supplier<Boolean> toggleIntake) {
        this.toggleIntake = toggleIntake;
        this.intake = intake;
        this.addRequirements(intake);
    }

    @Override
    public void execute() {
        if (toggleIntake.get()) {
            intake.run();
        } else {
            intake.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
    intake.stop();
    }
}

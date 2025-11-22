package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.subsystems.Intake;

public class RunIntake extends CommandBase {

    private Intake intake;
    private ToggleButtonReader toggleIntake;

    public RunIntake(Intake intake, ToggleButtonReader toggleIntake) {
        this.toggleIntake = toggleIntake;
        this.intake = intake;
        this.addRequirements(intake);
    }

    @Override
    public void execute() {
        if (toggleIntake.getState()) {
//            intake.run();
        } else {
//            intake.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
//     intake.stop();
    }
}

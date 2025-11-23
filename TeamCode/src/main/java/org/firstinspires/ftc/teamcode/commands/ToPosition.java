package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public class ToPosition extends CommandBase {
    // TODO - fix is in launcher temp-test!!!
    Position position;
    Launcher launcher;

    ToPosition(Position position, Launcher launcher) {
        this.position = position;
        this.launcher = launcher;
        addRequirements(launcher);
    }

    @Override
    public void initialize() {
        if (potentiometer.getVoltage() > position.val) launcher.set(1);
        else launcher.set(-1);
    }

    @Override
    public boolean isFinished() {
        return inRange(position.val);
    }

    @Override
    public void end(boolean interrupted) {
        launcher.set(0);
    }
}

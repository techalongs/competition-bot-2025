package org.firstinspires.ftc.teamcode.subsystems;

import static java.lang.Thread.sleep;

import android.telephony.TelephonyCallback;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.util.Timing;

import org.firstinspires.ftc.teamcode.util.RevPotentiometer;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Launcher extends SubsystemBase {
    private final CRServoEx launcher;
    private final RevPotentiometer potentiometer;
    private final double TOLERANCE = 0.01;

    private enum Position {
        LOAD(0.2),
        READY(0.75),
        FIRE(0.95);

        final double val;

        Position(double v) {
            this.val = v;
        }
    }

    private class MoveTo extends CommandBase {
        Position pos;

        MoveTo(Position pos) {
            this.pos = pos;
            addRequirements(Launcher.this);
        }

        @Override
        public void initialize() {
            if (potentiometer.getVoltage() > pos.val) launcher.set(1);
            else launcher.set(-1);
        }

        @Override
        public boolean isFinished() {
            return inRange(pos.val);
        }

        @Override
        public void end(boolean interrupted) {
            launcher.set(0);
        }
    }

    public Launcher(HardwareMap hardwareMap, String launch, String pot) {
        launcher = new CRServoEx(hardwareMap, launch);
        potentiometer = new RevPotentiometer(hardwareMap, pot);

        launcher.setInverted(false);
        launcher.setRunMode(CRServoEx.RunMode.RawPower);
    }

    private boolean inRange(double value) {
        return value - TOLERANCE < potentiometer.getVoltage()
                && potentiometer.getVoltage() < value + TOLERANCE;
    }

    public Command launch() {
        return new SequentialCommandGroup(
                new MoveTo(Position.READY),
                new SleepCommand(500),
                new MoveTo(Position.FIRE),
                new SleepCommand(500),
                new MoveTo(Position.LOAD),
                new SleepCommand(500),
                new MoveTo(Position.READY)
        );
    }
}

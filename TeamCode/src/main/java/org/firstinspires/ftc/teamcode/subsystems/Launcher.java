package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import org.firstinspires.ftc.teamcode.util.RevPotentiometer;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class Launcher extends SubsystemBase {
    // 1 = closed to intake
    // 2 = middle launcher
    // 3 = farthest from launcher

    private CRServoEx launcher;
    private RevPotentiometer potentiometer;

    private double TOLERANCE = 0.01;

    public Launcher() {

    }

    private enum Position {

    }

    private boolean inRange() {

    }

    public Command launch() {
        return new SequentialCommandGroup(
                new MoveTo(Position.FIRE),
                new SleepCommand(250),
                new MoveTo(Position.LOAD),
                new SleepCommand(250),
                new MoveTo(Position.READY)
        );
    }


}

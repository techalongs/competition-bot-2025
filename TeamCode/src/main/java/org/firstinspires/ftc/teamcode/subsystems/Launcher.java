package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import org.firstinspires.ftc.teamcode.util.RevPotentiometer;

public class Launcher extends SubsystemBase {
    // 1 = closed to intake
    // 2 = middle launcher
    // 3 = farthest from launcher

    private CRServoEx launcher;
    private RevPotentiometer potentiometer;

}

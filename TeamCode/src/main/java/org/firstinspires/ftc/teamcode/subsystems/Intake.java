package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

public class Intake extends SubsystemBase {
    private final MotorEx intake;

    public Intake(HardwareMap hardwareMap, String intakeName) {
        intake = new MotorEx(hardwareMap, intakeName);

        intake.setInverted(true);
        intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intake.stopAndResetEncoder();
        intake.setRunMode(Motor.RunMode.RawPower);
    }

    public void run() {
        intake.set(1);
    }

    public void stop() {
        intake.set(0);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.MotorGroup;

public class Lifter extends SubsystemBase {
    private final MotorGroup lifts;

    public Lifter(HardwareMap hardwareMap, String left, String right) {
        MotorEx leftLift = new MotorEx(hardwareMap, left);
        MotorEx rightLift = new MotorEx(hardwareMap, right);

        leftLift.setInverted(false);
        rightLift.setInverted(true);

        leftLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        leftLift.stopAndResetEncoder();
        rightLift.stopAndResetEncoder();

        leftLift.setRunMode(Motor.RunMode.VelocityControl);
        rightLift.setRunMode(Motor.RunMode.VelocityControl);

        lifts = new MotorGroup(leftLift, rightLift);
    }

    public void raise() {
        lifts.set(1);
    }

    public void lower() {
        lifts.set(-1);
    }

    public void stop() {
        lifts.set(0);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class Intake extends SubsystemBase {
    private final MotorEx intake;
    private final ServoEx fork;
    private boolean running = false;

    public Intake(HardwareMap hardwareMap, String intakeName, String forkName) {
        intake = new MotorEx(hardwareMap, intakeName);
        fork = new ServoEx(hardwareMap, forkName);

        intake.setInverted(false);
        intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intake.stopAndResetEncoder();
        intake.setRunMode(Motor.RunMode.RawPower);

        fork.setInverted(false);
    }

    public void run() {
        intake.set(1);
        running = true;
    }

    public void reverse() {
        intake.set(-1);
        running = true;
    }

    public void stop() {
        intake.set(0);
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void turnForkRight() {
        fork.set(1);
    }

    public void turnForkLeft() {
        fork.set(0);
    }

    public void resetFork() {
        fork.set(0.4);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

public class Intake extends SubsystemBase {
    private final MotorEx intake;
    private final ServoEx rightFork;
    private final ServoEx leftFork;
    private boolean running = false;

    public Intake(HardwareMap hardwareMap, String intakeName, String rightForkName, String leftForkName) {
        intake = new MotorEx(hardwareMap, intakeName);
        rightFork = new ServoEx(hardwareMap, rightForkName);
        leftFork = new ServoEx(hardwareMap, leftForkName);

        intake.setInverted(false);
        intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intake.stopAndResetEncoder();
        intake.setRunMode(Motor.RunMode.RawPower);

        rightFork.setInverted(false);
        leftFork.setInverted(true);
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

    public void turnLeftFork() {
        leftFork.set(0);
    }
    public void turnRightFork() {
        rightFork.set(0.6);
    }

    public void resetLeftFork() {
        leftFork.set(0.6);
    }

    public void resetRightFork() {
        rightFork.set(0);
    }
}

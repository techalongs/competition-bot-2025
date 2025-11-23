package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends SubsystemBase {

    private MotorEx motorIntake;
    private Telemetry telemetry;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry, String intakeName) {

        motorIntake = new MotorEx(hardwareMap, intakeName);

        motorIntake.setInverted(true);

        motorIntake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        motorIntake.stopAndResetEncoder();

        motorIntake.setRunMode(Motor.RunMode.RawPower);

    }


    public void run() {
        motorIntake.set(1);
    }

    public void stop() {
        motorIntake.set(0);
    }

}

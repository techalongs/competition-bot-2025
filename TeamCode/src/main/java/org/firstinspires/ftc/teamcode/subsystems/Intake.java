package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends SubsystemBase {

    private MotorEx motorIntakeBubbler;
    private CRServoEx servoIntakeGrabber;
    private Servo servoArtifactLifter;
    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {

        motorIntakeBubbler = new MotorEx(hardwareMap, "intakeLift");
        servoArtifactLifter = hardwareMap.get(Servo.class, "liftToIntake");
        servoIntakeGrabber = hardwareMap.get(CRServoEx.class, "intakeGrabber");
//        TODO - Check device name for servoIntakeGrabber

        motorIntakeBubbler.setInverted(true);
        servoIntakeGrabber.setInverted(false);

        motorIntakeBubbler.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        motorIntakeBubbler.stopAndResetEncoder();

        motorIntakeBubbler.setRunMode(Motor.RunMode.RawPower);
        servoIntakeGrabber.setRunMode(CRServoEx.RunMode.RawPower);

        servoArtifactLifter.setPosition(1.0);

    }

    public void raiseServoLifter() {
        servoArtifactLifter.setPosition(0.65);
    }

    public void lowerServoLifter() {
        servoArtifactLifter.setPosition(0.0);
    }


    public void startGrabber() {
        servoIntakeGrabber.set(1.0);
    }

    public void stopGrabber() {
        servoIntakeGrabber.set(0.0);
    }

    public void startBubbler() {
        motorIntakeBubbler.set(1.0);
    }

    public void stopBubbler() {
        motorIntakeBubbler.set(0.0);
    }

    public void startElevator() {
        this.startBubbler();
        this.raiseServoLifter();
    }

}

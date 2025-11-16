package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

public class IntakeSubsystem extends SubsystemBase {
    private final MotorEx intake;
    private final CRServoEx bubbler;
    private final ServoEx lifter;

    public IntakeSubsystem(HardwareMap hardwareMap, String intakeName, String bubblerName, String lifterName) {
        intake = new MotorEx(hardwareMap, intakeName);
        bubbler = new CRServoEx(hardwareMap, bubblerName);
        lifter = new ServoEx(hardwareMap, lifterName);

        intake.setInverted(true);
        bubbler.setInverted(false);
        lifter.setInverted(false);

        intake.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        intake.stopAndResetEncoder();
        intake.setRunMode(Motor.RunMode.RawPower);
        bubbler.setRunMode(CRServoEx.RunMode.RawPower);
    }
}

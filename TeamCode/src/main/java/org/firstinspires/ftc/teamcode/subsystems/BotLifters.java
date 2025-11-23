package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BotLifters extends SubsystemBase {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private double upSpeedLimiter = 0.5;
    private double downSpeedLimiter = 0.5;
    private final double holdPower = 0.5;
    private final int holdThresholdPosition = 800;
    private int holdTargetPosition;
    private DcMotor.RunMode currentRunMode;
    private String status = "None";
    private String statusDetail = " ";

    public BotLifters(HardwareMap hardwareMap, Telemetry telemetry) { //, TouchSensor topLimitSensor, TouchSensor bottomLimitSensor) {
        leftMotor = initBasicDcMotor("leftLift", hardwareMap);
        rightMotor = initBasicDcMotor("rightLift", hardwareMap);
        setTargetPosition(0);
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER, true);
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER, true);
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private DcMotor initBasicDcMotor(String name, HardwareMap hardwareMap) {
        DcMotor motor = hardwareMap.get(DcMotor.class, name);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        this.setRunMode(runMode, false);
    }

    public void setRunMode(DcMotor.RunMode runMode, boolean force) {
        if (force || currentRunMode != runMode) {
            leftMotor.setMode(runMode);
            rightMotor.setMode(runMode);
            currentRunMode = runMode;
        }
    }

    public DcMotor.RunMode getCurrentRunMode() {
        return currentRunMode;
    }

    public int getCurrentPosition() {
        if (leftMotor != null) {
            return leftMotor.getCurrentPosition();
        } else if (rightMotor != null) {
            return rightMotor.getCurrentPosition();
        } else {
            throw new RuntimeException("Both motors are null!");
        }
    }

    public void setTargetPosition(int position) {
        if (leftMotor != null) {
            leftMotor.setTargetPosition(position);
        }
        if (rightMotor != null) {
            rightMotor.setTargetPosition(position);
        }
    }

    public double getPower() {
        if (leftMotor != null) {
            return leftMotor.getPower();
        } else if (rightMotor != null) {
            return rightMotor.getPower();
        } else {
            throw new RuntimeException("Both motors are null!");
        }
    }

    public void setPower(double power) {
        if (leftMotor != null) {
            leftMotor.setPower(power);
        }
        if (rightMotor != null) {
            rightMotor.setPower(power);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        if (leftMotor != null) {
            leftMotor.setZeroPowerBehavior(behavior);
        } else if (rightMotor != null) {
            rightMotor.setZeroPowerBehavior(behavior);
        }
    }

    public void setSpeedLimiter(double speedLimiter) {
        setUpSpeedLimiter(speedLimiter);
        setDownSpeedLimiter(speedLimiter);
    }

    public void setUpSpeedLimiter(double upSpeedLimiter) {
        this.upSpeedLimiter = upSpeedLimiter;
    }

    public double getUpSpeedLimiter() {
        return upSpeedLimiter;
    }

    public void setDownSpeedLimiter(double downSpeedLimiter) {
        this.downSpeedLimiter = downSpeedLimiter;
    }

    public double getDownSpeedLimiter() {
        return downSpeedLimiter;
    }

    public DcMotor getLeftMotor() {
        return leftMotor;
    }

    public DcMotor getRightMotor() {
        return rightMotor;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }
}

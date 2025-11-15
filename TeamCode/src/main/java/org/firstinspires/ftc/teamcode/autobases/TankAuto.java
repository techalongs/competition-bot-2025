package org.firstinspires.ftc.teamcode.autobases;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Tank Autonomous")
public class TankAuto extends LinearOpMode {
    private DcMotor rightMotor;
    private DcMotor leftMotor;

    private void initMotors() {
        // TODO: Add config ids for motors
        rightMotor = hardwareMap.get(DcMotor.class, "");
        leftMotor = hardwareMap.get(DcMotor.class, "");

        // TODO: Change which motors are reversed
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // DO NOT CHANGE
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void setMotors(double power) {
        rightMotor.setPower(power);
        leftMotor.setPower(power);
    }

    @Override
    public void runOpMode() {
        // Initialize all motors
        initMotors();

        // Print status - indicate that auto is ready run
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        // Create a timer
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        // Keep motors running for 3 seconds
        while (timer.time(TimeUnit.SECONDS) <= 3) {
            setMotors(1);

            // Update telemetry
            telemetry.addData("Front Right Power", rightMotor.getPower());
            telemetry.addData("Front Left Power", leftMotor.getPower());
            telemetry.update();
        }

        // Stop all motors
        setMotors(0);

        // Print finished status
        telemetry.addData("Status", "Finished");
        telemetry.addData("Front Right Power", rightMotor.getPower());
        telemetry.addData("Front Left Power", leftMotor.getPower());
        telemetry.update();

        // Allow for a 3 second pause to read telemetry if needed
        sleep(3000);
    }
}

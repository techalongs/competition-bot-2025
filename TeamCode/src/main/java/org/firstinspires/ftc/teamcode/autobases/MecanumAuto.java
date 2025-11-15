package org.firstinspires.ftc.teamcode.autobases;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Mecanum Autonomous")
public class MecanumAuto extends LinearOpMode {
    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private void initMotors() {
        // TODO: Add config ids for motors
        frontRight = hardwareMap.get(DcMotor.class, "");
        frontLeft = hardwareMap.get(DcMotor.class, "");
        backRight = hardwareMap.get(DcMotor.class, "");
        backLeft = hardwareMap.get(DcMotor.class, "");

        // TODO: Change which motors are reversed
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        // DO NOT CHANGE
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void setMotors(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
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

        // Go forward for half a second
        while (timer.time(TimeUnit.SECONDS) <= 0.5 && opModeIsActive()) {
            setMotors(0.5);

            // Update telemetry
            telemetry.addData("Time left", 1000 - timer.time(TimeUnit.MILLISECONDS));
            telemetry.addData("Front Left Power", frontLeft.getPower());
            telemetry.addData("Front Right Power", frontRight.getPower());
            telemetry.addData("Back Left Power", backLeft.getPower());
            telemetry.addData("Back Right Power", backRight.getPower());
            telemetry.update();
        }

        // Stop all motors
        setMotors(0);

        // Print finished status
        telemetry.addData("Status", "Finished");
        telemetry.addData("Front Left Power", frontLeft.getPower());
        telemetry.addData("Front Right Power", frontRight.getPower());
        telemetry.addData("Back Left Power", backLeft.getPower());
        telemetry.addData("Back Right Power", backRight.getPower());
        telemetry.update();

        // Allow for a 3 second pause to read telemetry if needed
        sleep(3000);
    }
}

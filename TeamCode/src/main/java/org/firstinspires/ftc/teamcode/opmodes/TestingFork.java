package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestingFork extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Servo rightFork = hardwareMap.get(Servo.class, "leftFork");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.square) rightFork.setPosition(0);
            else if (gamepad1.circle) rightFork.setPosition(0.6);
            else if (gamepad1.cross) rightFork.setPosition(0.4);

            telemetry.addData("Position", rightFork.getPosition());
            telemetry.addData("Status", "Active");
            telemetry.update();
        }
    }
}

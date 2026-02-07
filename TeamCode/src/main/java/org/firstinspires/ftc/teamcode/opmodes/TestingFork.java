package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestingFork extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Servo fork = hardwareMap.get(Servo.class, "intakeFork");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.square) fork.setPosition(0);
            else if (gamepad1.circle) fork.setPosition(1);
            else if (gamepad1.cross) fork.setPosition(0.4);

            telemetry.addData("Position", fork.getPosition());
            telemetry.addData("Status", "Active");
            telemetry.update();
        }
    }
}

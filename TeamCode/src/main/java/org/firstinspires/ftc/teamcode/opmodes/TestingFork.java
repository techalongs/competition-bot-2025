package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class TestingFork extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        CRServo fork = hardwareMap.get(CRServo.class, "intakeFork");
        TouchSensor forkLimit = hardwareMap.get(TouchSensor.class, "forkLimit");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.square) {
                while (!forkLimit.isPressed()) {
                    fork.setPower(-1);
                }

                fork.setPower(0);
            } else if (gamepad1.circle) {
                while (!forkLimit.isPressed()) {
                    fork.setPower(-1);
                }

                fork.setPower(0);
            }

            telemetry.addData("Limit", forkLimit.isPressed());
            telemetry.addData("Status", "Active");
            telemetry.update();
        }
    }
}

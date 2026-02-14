package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@TeleOp(name = "Drive Robot-Centric", group = "Test")
public class RobotCentricDrive extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;

    @Override
    public void init() {
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
    }

    @Override
    public void loop() {
        robot.drive(Drivetrain.DriveState.ROBOT_CENTRIC, driver1, 0.5);
        return null;
    }

}

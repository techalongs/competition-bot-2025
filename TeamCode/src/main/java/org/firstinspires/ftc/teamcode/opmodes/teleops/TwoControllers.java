package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.opmodes.controls.ControlsV2;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.controls.GamepadControls;

import java.util.Arrays;

@TeleOp(name = "Two Controller TeleOp", group = "Normal Controls")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;

    private GamepadControls gamepadControls;

    @Override
    public void init() {
        this.telemetry = new JoinedTelemetry(this.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        gamepadControls = new ControlsV2(driver1, driver2, robot);
    }

    @Override
    public void loop() {
        loopReadStuff();

        robot.drive(Drivetrain.DriveState.ROBOT_CENTRIC, driver1, RobotConfig.driveSpeedLimit);

        telemetry.addData("Launcher Colors", Arrays.toString(robot.getLauncherColors()));
        telemetry.addData("Launcher Power State", RobotConfig.launcherPower.name());
        telemetry.addData("Drive Speed", RobotConfig.driveSpeedLimit);
        telemetry.update();
    }

    public void loopReadStuff() {
        CommandScheduler.getInstance().run();
        driver1.readButtons();
        driver2.readButtons();
    }

}

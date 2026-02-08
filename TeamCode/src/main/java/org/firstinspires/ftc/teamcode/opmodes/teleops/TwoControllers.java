package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.opmodes.controls.ControlsV2;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.controls.GamepadControls;
import org.firstinspires.ftc.teamcode.util.REVColorSensor;

import java.util.Arrays;

import lombok.Setter;

@TeleOp(name = "Two Controller TeleOp", group = "Normal Controls")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private REVColorSensor sensor1;
    private REVColorSensor sensor2;

    private Drivetrain.DriveState driveState = Drivetrain.DriveState.ROBOT_CENTRIC;
    @Setter
    private volatile double driveSpeedLimit = RobotConfig.driveFastSpeedLimit;
    private GamepadControls gamepadControls;

    @Override
    public void init() {
        this.telemetry = new JoinedTelemetry(this.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        sensor1 = new REVColorSensor(hardwareMap, "rightSensor1");
        sensor2 = new REVColorSensor(hardwareMap, "rightSensor2");

        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        commandScheduler = CommandScheduler.getInstance();

        gamepadControls = new ControlsV2(driver1, driver2, robot, this::setDriveSpeedLimit);
    }

    @Override
    public void loop() {
        loopReadStuff();

        robot.drive(driveState, driver1, driveSpeedLimit);

        telemetry.addData("Launcher Colors", Arrays.toString(robot.getLauncherColors()));
        telemetry.addData("Launcher Power State", RobotConfig.launcherPower.name());
        // Are these needed anymore?
        //telemetry.addData("Drive", getDriveState());
        //telemetry.addData("Sensor 1", sensor1.RGBtoHSV(sensor1.red(), sensor1.green(), sensor1.blue(), new float[3])[0]);
        //telemetry.addData("Sensor 2", sensor2.RGBtoHSV(sensor2.red(), sensor2.green(), sensor2.blue(), new float[3])[0]);
        telemetry.update();
    }

    public void loopReadStuff() {
        commandScheduler.run();
        driver1.readButtons();
        driver2.readButtons();
    }

}

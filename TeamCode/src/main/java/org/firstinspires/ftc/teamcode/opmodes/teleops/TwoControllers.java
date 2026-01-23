package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.AnnaControls;
import org.firstinspires.ftc.teamcode.subsystems.DefaultControls;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.GamepadControls;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.REVColorSensor;

import java.util.Arrays;

@TeleOp(name = "Two Controller TeleOp - PS4", group = "Normal Controls")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private boolean intakeState = false;
    private REVColorSensor sensor1;
    private REVColorSensor sensor2;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;
    private GamepadControls gamepadControls;
    private Launcher.Power launcherPower = Launcher.Power.SHORT;
    private final Launcher.Power[] launcherPowers =
            new Launcher.Power[]{Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};
    private int launcherState = 2;

    // Long - red, Mid - blue, Close - green
    private final int[][] gamepadColors = new int[][]{{255, 0, 0}, {0, 0, 255}, {0, 255, 0}};

    @Override
    public void init() {
        sensor1 = new REVColorSensor(hardwareMap, "rightSensor1");
        sensor2 = new REVColorSensor(hardwareMap, "rightSensor2");

        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        commandScheduler = CommandScheduler.getInstance();

        gamepadControls = new DefaultControls(driver1, driver2, robot);
        toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
        toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();

        driver2.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver2.getGamepadButton(GamepadKeys.Button.CROSS)).toggleWhenActive(() -> {
                    commandScheduler.clearButtons();
                    gamepadControls = new AnnaControls(driver1, driver2, robot);
                    toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
                    toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();

                },
                () -> {
                    commandScheduler.clearButtons();
                    gamepadControls = new DefaultControls(driver1, driver2, robot);
                    toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
                    toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();

                });
    }

    @Override
    public void loop() {
        loopReadStuff();

        double driveSpeedLimit = getDriveSpeedLimit();
        Drivetrain.DriveState driveState = getDriveState();
        robot.drive(driveState, driver1, driveSpeedLimit);

        telemetry.addData("Sensor 1", sensor1.RGBtoHSV(sensor1.red(), sensor1.green(), sensor1.blue(), new float[3])[0]);
        telemetry.addData("Sensor 2", sensor2.RGBtoHSV(sensor2.red(), sensor2.green(), sensor2.blue(), new float[3])[0]);
        telemetry.addData("Launcher Colors", Arrays.toString(robot.getLauncherColors()));
        telemetry.addData("Launcher Power State", launcherPower.name());
        telemetry.addData("Drive state", getDriveState());
        if (!gamepadControls.isDefault()) {
            telemetry.addData("Countrols", gamepadControls);
            telemetry.addLine(" Option + A or Cross to switch back");
        }
        telemetry.update();
    }

    public double getDriveSpeedLimit() {
        if (toggleDriveSlow.getState()) {
            return driveSlowSpeedLimit;
        } else {
            return driveFastSpeedLimit;
        }
    }

    public Drivetrain.DriveState getDriveState() {
        if (toggleFieldCentric.getState()) {
            return Drivetrain.DriveState.FIELD_CENTRIC;
        } else {
            return Drivetrain.DriveState.ROBOT_CENTRIC;
        }
    }

    public void loopReadStuff() {
        commandScheduler.run();
        driver1.readButtons();
        driver2.readButtons();
        toggleDriveSlow.readValue();
        toggleFieldCentric.readValue();
    }
}

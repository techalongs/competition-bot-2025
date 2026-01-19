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
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.REVColorSensor;

import java.util.Arrays;

@TeleOp(name = "Two Controllers TeleOp - Logitech", group = "Logitech Controls")
public class TwoControllersLogitech extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private boolean intakeState = false;
    private REVColorSensor sensor1;
    private REVColorSensor sensor2;
    private REVColorSensor sensor3;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;
    private Launcher.Power launcherPower = Launcher.Power.SHORT;
    private final Launcher.Power[] launcherPowers =
            new Launcher.Power[] {Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};
    private int launcherState = 2;

    @Override
    public void init() {
        sensor1 = new REVColorSensor(hardwareMap, "sensor1");
        sensor2 = new REVColorSensor(hardwareMap, "sensor2");
        sensor3 = new REVColorSensor(hardwareMap, "sensor3");

        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        commandScheduler = CommandScheduler.getInstance();

        // With multi button toggles there is and enter before the .and
        //  Drive Slow Toggle = Options + Left Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER))::get);

        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.X))::get);

        // Launcher Power Toggle - Right Bumper
        driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(
                        new InstantCommand(() -> launcherPower = launcherPowers[++launcherState % launcherPowers.length])
                );

        // Intake - Y
        driver1.getGamepadButton(GamepadKeys.Button.Y)
                .whenPressed(new ConditionalCommand(
                        robot.runIntake(),
                        robot.stopIntake(),
                        () -> {
                            intakeState = !intakeState;
                            return intakeState;
                        }
                ));

        // Launchers - X
        driver2.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.PURPLE, launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.B)
                .whenPressed(new DeferredCommand(() -> robot.launchColor(Launcher.Color.GREEN, launcherPower), null));
        driver2.getGamepadButton(GamepadKeys.Button.A)
                .whenPressed(new DeferredCommand(() -> robot.launchAll(launcherPower), null));

        // Ascent Lifts - Dpad Up and Dpad Down
//        driver1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileHeld(robot.raiseLifts());
//        driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whileHeld(robot.lowerLifts());
    }

    @Override
    public void loop() {
        loopReadStuff();

        double driveSpeedLimit = getDriveSpeedLimit();
        Drivetrain.DriveState driveState = getDriveState();
        robot.drive(driveState, driver1, driveSpeedLimit);

        telemetry.addData("Sensor 1", sensor1.RGBtoHSV(sensor3.red(), sensor3.green(), sensor3.blue(), new float[3])[0]);
        telemetry.addData("Sensor 2", sensor2.RGBtoHSV(sensor2.red(), sensor2.green(), sensor2.blue(), new float[3])[0]);
        telemetry.addData("Sensor 3", sensor3.RGBtoHSV(sensor3.red(), sensor3.green(), sensor3.blue(), new float[3])[0]);
        telemetry.addData("Launcher Colors", Arrays.toString(robot.getLauncherColors()));
        telemetry.addData("Launcher Power State", launcherPower.name());
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

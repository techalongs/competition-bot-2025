package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.util.RevPotentiometer;

@TeleOp(name = "Two Controller TeleOp")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private boolean intakeState = false;
    private RevPotentiometer pot;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;

    @Override
    public void init() {
        pot = new RevPotentiometer(hardwareMap, "pot3");
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap, telemetry);
        commandScheduler = CommandScheduler.getInstance();

        // With multi button toggles there is and enter before the .and
        //  Drive Slow Toggle = X + Right Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.X)
                .and(driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER))::get);

        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.X))::get);

        // Intake - Y
        driver1.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new ConditionalCommand(
                robot.runIntake(),
                robot.stopIntake(),
                () -> {
                    intakeState = !intakeState;
                    return intakeState;
                }
        ));

        // Launchers - X
        driver2.getGamepadButton(GamepadKeys.Button.X).whenPressed(robot.launchBack());
        driver2.getGamepadButton(GamepadKeys.Button.A).whenPressed(robot.launchMid());
        driver2.getGamepadButton(GamepadKeys.Button.B).whenPressed(robot.launchFront());
        driver2.getGamepadButton(GamepadKeys.Button.Y)
                .and(driver2.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER))
                .whenActive(robot.launchAll());

        // Ascent Lifts - Dpad Up and Dpad Down
        driver1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileHeld(robot.raiseLifts());
        driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whileHeld(robot.lowerLifts());
    }

    @Override
    public void loop() {
        loopReadStuff();

        double driveSpeedLimit = getDriveSpeedLimit();
        Robot.DriveState driveState = getDriveState();
        robot.drive(driveState, driver1, driveSpeedLimit);

        telemetry.addData("Drive Mode", driveState.name());
        telemetry.addData("Voltage", pot.getVoltage());
        telemetry.update();
    }

    public double getDriveSpeedLimit() {
        if (toggleDriveSlow.getState()) {
            return driveSlowSpeedLimit;
        } else {
            return driveFastSpeedLimit;
        }
    }

    public Robot.DriveState getDriveState() {
        if (toggleFieldCentric.getState()) {
            return Robot.DriveState.FIELD_CENTRIC;
        } else {
            return Robot.DriveState.ROBOT_CENTRIC;
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

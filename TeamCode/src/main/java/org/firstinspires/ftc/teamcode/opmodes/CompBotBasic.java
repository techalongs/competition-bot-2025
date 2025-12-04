package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.commands.EndBotLifters;
import org.firstinspires.ftc.teamcode.commands.RunBotLifter;
import org.firstinspires.ftc.teamcode.commands.RunIntake;
import org.firstinspires.ftc.teamcode.commands.StartBotLifters;

@TeleOp(name = "Comp Basic")
public class CompBotBasic extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private ToggleButtonReader toggleIntake;
    private ToggleButtonReader toggleHelpTelemetry;
    private RunIntake runIntake;
    private RunBotLifter runBotLifter;
    private RunBotLifter upBotLifter;
    private RunBotLifter downBotLifter;
    private StartBotLifters startBotLifters;
    private EndBotLifters endBotLifters;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;

    @Override
    public void init() {
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap, telemetry);
        commandScheduler = CommandScheduler.getInstance();

        // with multi button toggles there is an enter berfore the .and/.or
        // Drive Slow Toggle = X + Right Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.X)
                .and(driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER))::get);
        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.X))::get);
        // Intake the artifacts up = Y
        toggleIntake = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)::get);
        runIntake = new RunIntake(robot.getIntake(), toggleIntake::getState);
        commandScheduler.schedule(runIntake);
        // Telemetry - control display =
        toggleHelpTelemetry = new ToggleButtonReader(
                driver1.getGamepadButton(GamepadKeys.Button.X).and(driver1.getGamepadButton(GamepadKeys.Button.Y))
                                .or(driver2.getGamepadButton(GamepadKeys.Button.X))
                                .and(driver2.getGamepadButton(GamepadKeys.Button.Y)))
        // Lift the bot up =
//        upBotLifter = new RunBotLifter(driver1.getGamepadButton(GamepadKeys.Button.DPAD_UP).whileHeld(runBotLifter)::get);
//        driver1.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whileHeld(runBotLifter);
//        commandScheduler.schedule(runBotLifter);

    }

    @Override
    public void loop() {
        loopReadStuff();

        double driveSpeedLimit = getDriveSpeedLimit();
        Robot.DriveState driveState = getDriveState();


        robot.drive(driveState, driver1, driveSpeedLimit);

        telemetry.addData("Drive Mode", driveState.name());
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
        toggleIntake.readValue();

    }
}

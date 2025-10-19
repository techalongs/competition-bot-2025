package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "Comp Basic")
public class CompBotBasic extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private ToggleButtonReader toggleIntakeLift;
    private ToggleButtonReader toggleTelemetry;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;

    @Override
    public void init() {
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap, telemetry);
        commandScheduler = CommandScheduler.getInstance();


        // with multi button toggles there is and enter berfore the .and
        // Drive Slow Toggle = X + Right Bumper
        toggleDriveSlow = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.X)
                .and(driver1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER))::get);
        // Drive Field Centric Toggle = Options + X
        toggleFieldCentric = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .and(driver1.getGamepadButton(GamepadKeys.Button.X))::get);
        // Intake Lift the artifacts up = Y
        toggleIntakeLift = new ToggleButtonReader(driver1.getGamepadButton(GamepadKeys.Button.Y)::get);
        //
    }

    @Override
    public void loop() {
        loopReadStuff();

        if (driver1.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            robot.servoLifter();
        }

        double driveSpeedLimit = getDriveSpeedLimit();
        Robot.DriveState driveState = getDriveState();
        runMotorIntakeLiftLoop();

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

    public void runMotorIntakeLiftLoop() {
        if (toggleIntakeLift.getState()){
            robot.runIntakeLift(1.0);
        }else {
            robot.runIntakeLift(0.0);
        }
    }

    public void runServoIntakeLift() {

    }

    public void loopReadStuff() {
        commandScheduler.run();
        driver1.readButtons();
        driver2.readButtons();
        toggleDriveSlow.readValue();
        toggleFieldCentric.readValue();
        toggleIntakeLift.readValue();
    }
}

package org.firstinspires.ftc.teamcode.sandbox;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.List;

/**
 * Special OpMode to help tune the hammers.
 */
@Configurable
@TeleOp(name = "Tune Hammers", group = "Sandbox")
public class TuneHammersOpMode extends OpMode {

    private Robot robot;
    private GamepadEx driver;

    private Launcher leftLauncher;
    private Launcher midLauncher;
    private Launcher rightLauncher;

    private List<LynxModule> allHubs;
    private String motorInfo;

    /**
     *
     */
    @Override
    public void init() {
        this.telemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), this.telemetry);
        allHubs = hardwareMap.getAll(LynxModule.class);

        robot = new Robot(hardwareMap);
        leftLauncher = robot.getLeftLauncher();
        midLauncher = robot.getMidLauncher();
        rightLauncher = robot.getRightLauncher();

        MotorEx m = midLauncher.getMotor();
        motorInfo = "CPR=" + m.getCPR();
        motorInfo += "; MaxRPM=" + m.getMaxRPM();
        motorInfo += "; MaxTPS=" + m.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        driver = new GamepadEx(gamepad1);

        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)).whenActive(() -> {
            RobotConfig.launchRawPower += 0.05;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)).whenActive(() -> {
            RobotConfig.launchRawPower -= 0.05;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)).whenActive(() -> {
            RobotConfig.sleepBetweenSequentialLaunches -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)).whenActive(() -> {
            RobotConfig.sleepBetweenSequentialLaunches += 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)).whenActive(() -> {
            RobotConfig.sleepBeforeLaunch -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)).whenActive(() -> {
            RobotConfig.sleepBeforeLaunch += 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.TRIANGLE)).whenActive(() -> {
            RobotConfig.sleepAfterLaunch += 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.CROSS)).whenActive(() -> {
            RobotConfig.sleepAfterLaunch -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.SQUARE)).whenActive(() -> {
            RobotConfig.reloadPower -= 0.05;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.CIRCLE)).whenActive(() -> {
            RobotConfig.reloadPower += 0.05;
        });

        // Launch All
        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(new DeferredCommand(() -> robot.launchAll(RobotConfig::getLaunchRawPower), null));
        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5)
                .whenActive(new DeferredCommand(() -> robot.launchLeft(RobotConfig::getLaunchRawPower), null));
        // Launch Left
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(new DeferredCommand(() -> robot.launchLeft(RobotConfig::getLaunchRawPower), null));

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        this.readLoop();

        // Drive
        robot.drive(Drivetrain.DriveState.ROBOT_CENTRIC, driver, 0.5);

        this.logLoop();
    }

    @Override
    public void start() {
        telemetry.addData("Status", "Started");
    }

    @Override
    public void stop() {
        telemetry.addData("Status", "Stopped");
    }

    private void readLoop() {
        driver.readButtons();
        CommandScheduler.getInstance().run();
    }

    private void logLoop() {
        double leftAmps = leftLauncher.getMotor().getCurrent(CurrentUnit.AMPS);
        double midAmps = midLauncher.getMotor().getCurrent(CurrentUnit.AMPS);
        double rightAmps = rightLauncher.getMotor().getCurrent(CurrentUnit.AMPS);

        telemetry.addData("Launch Power", "%.2f", RobotConfig.launchRawPower);
        telemetry.addData("Reload Power", "%.2f", RobotConfig.reloadPower);

        telemetry.addLine("SLEEP ==========");
        if (RobotConfig.sleepBeforeReload > 0) {
            telemetry.addData("  < Before Reload", RobotConfig.sleepBeforeReload);
        }
        telemetry.addData("  • Before Launch", RobotConfig.sleepBeforeLaunch);
        telemetry.addData("  • After Launch", RobotConfig.sleepAfterLaunch);
        telemetry.addData("  > Between Launchers", RobotConfig.sleepBetweenSequentialLaunches);

        telemetry.addLine("MOTOR CURRENT ==========");
        telemetry.addData("  • L", "%.2f A", leftAmps);
        telemetry.addData("  • M", "%.2f A", midAmps);
        telemetry.addData("  • R", "%.2f A", rightAmps);

        telemetry.addLine("HUBS ==========");
        for (LynxModule hub : allHubs) {
            String hubType = hub.isParent() ? "Control Hub" : "Expansion Hub";
            // Battery voltage (12V input from XT30 connector)
            double voltage = hub.getInputVoltage(VoltageUnit.VOLTS);

            // Total current draw for this hub
            double totalCurrent = hub.getCurrent(CurrentUnit.AMPS);

            // Breakdown by bus
//            double gpioCurrent = hub.getGpioBusCurrent(CurrentUnit.MILLIAMPS);
//            double i2cCurrent = hub.getI2cBusCurrent(CurrentUnit.MILLIAMPS);

            telemetry.addData("  Hub", hubType);
            telemetry.addData("    • Voltage", "%.2f V", voltage);
            telemetry.addData("    • Total Current", "%.2f A", totalCurrent);
//            telemetry.addData("    • GPIO Bus", "%.1f mA", gpioCurrent);
//            telemetry.addData("    • I2C Bus", "%.1f mA", i2cCurrent);
        }

        telemetry.addLine("INFO ==========");
        telemetry.addData("  • Motor Info", motorInfo);

    }

}

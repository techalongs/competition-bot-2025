package org.firstinspires.ftc.teamcode.sandbox;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

import java.util.List;

/**
 * Special OpMode to help tune the hammers.
 */
@Configurable
@TeleOp(name = "Tune Hammers", group = "Sandbox")
public class TuneHammersOpMode extends OpMode {

    private static final double LAUNCH_POWER_SHORT = Launcher.Power.SHORT.power;
    private static final double LAUNCH_POWER_MID = Launcher.Power.MID.power;
    private static final double LAUNCH_POWER_LONG = Launcher.Power.LONG.power;

    public static double TH_launchPower = LAUNCH_POWER_SHORT;
    public static int TH_sleepBeforeReload = 0;
    public static int TH_sleepBeforeLaunch = 200;
    public static int TH_sleepAfterLaunch = 250;
    public static int TH_sleepBetweenLaunchers = 100;
    public static double TH_reloadPower = -1;

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
        TH_launchPower = LAUNCH_POWER_SHORT;

        // Launch Power
        // Short: SQUARE, Mid: TRIANGLE, Long: CIRCLE
        driver.getGamepadButton(GamepadKeys.Button.SQUARE).whenPressed(new InstantCommand(() -> TH_launchPower = LAUNCH_POWER_SHORT));
        driver.getGamepadButton(GamepadKeys.Button.TRIANGLE).whenPressed(new InstantCommand(() -> TH_launchPower = LAUNCH_POWER_MID));
        driver.getGamepadButton(GamepadKeys.Button.CIRCLE).whenPressed(new InstantCommand(() -> TH_launchPower = LAUNCH_POWER_LONG));

        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)).whenActive(() -> {
            TH_launchPower += 0.05;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)).whenActive(() -> {
            TH_launchPower -= 0.05;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)).whenActive(() -> {
            TH_sleepBetweenLaunchers -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.SHARE).and(driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)).whenActive(() -> {
            TH_sleepBetweenLaunchers += 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.TRIANGLE)).whenActive(() -> {
            TH_sleepAfterLaunch += 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.CROSS)).whenActive(() -> {
            TH_sleepAfterLaunch -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.SQUARE)).whenActive(() -> {
            TH_sleepBeforeReload -= 5;
        });
        driver.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver.getGamepadButton(GamepadKeys.Button.CIRCLE)).whenActive(() -> {
            TH_sleepBeforeReload += 5;
        });

        // Launch All
        new Trigger(() -> driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5)
                .whenActive(new DeferredCommand(() -> this.launchAll(TH_launchPower), null));
        // Launch Left
        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new DeferredCommand(() -> this.launchLeft(TH_launchPower), null));
        // Launch Mid
        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(new DeferredCommand(() -> this.launchMid(TH_launchPower), null));
        // Launch Right
        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                .whenPressed(new DeferredCommand(() -> this.launchRight(TH_launchPower), null));

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        this.readLoop();

        // Drive
        robot.drive(Drivetrain.DriveState.ROBOT_CENTRIC, driver, 0.5);

        this.logLoop();
    }

    /**
     *
     */
    private Command launch(Launcher launcher, double power) {
        return new SequentialCommandGroup(
                new SleepCommand(TH_sleepBeforeReload),
                new InstantCommand(() -> launcher.reload(TH_reloadPower)),
                new SleepCommand(TH_sleepBeforeLaunch),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(TH_sleepAfterLaunch),
                new InstantCommand(launcher::stopLauncher)
        );
    }

    private Command launchWithDelay(Launcher launcher, double power, int delayMs) {
        return new SequentialCommandGroup(
                new SleepCommand(delayMs),
                new InstantCommand(() -> launcher.reload(TH_reloadPower)),
                new SleepCommand(TH_sleepBeforeLaunch),
                new InstantCommand(() -> launcher.launch(power)),
                new SleepCommand(TH_sleepAfterLaunch),
                new InstantCommand(launcher::stopLauncher)
        );
    }

    /**
     *
     */
    public Command launchAll(double power) {
        return new SequentialCommandGroup(
                this.launch(leftLauncher, power),
                new SleepCommand(TH_sleepBetweenLaunchers),
                this.launch(midLauncher, power),
                new SleepCommand(TH_sleepBetweenLaunchers),
                this.launch(rightLauncher, power)
        );
    }

    /**
     *
     */
    public Command launchLeft(double power) {
        return this.launch(leftLauncher, power);
    }

    /**
     *
     */
    public Command launchMid(double power) {
        return this.launch(midLauncher, power);
    }

    /**
     *
     */
    public Command launchRight(double power) {
        return this.launch(rightLauncher, power);
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

        telemetry.addData("Launch Power", "%.2f", TH_launchPower);

        telemetry.addLine("SLEEP ==========");
        telemetry.addData("  < Before Reload", TH_sleepBeforeReload);
        telemetry.addData("  • Before Launch", TH_sleepBeforeLaunch);
        telemetry.addData("  • After Launch", TH_sleepAfterLaunch);
        telemetry.addData("  > Between Launchers", TH_sleepBetweenLaunchers);

        telemetry.addLine("MOTOR CURRENT ==========");
        telemetry.addData("  • L", "%.2f A", leftAmps);
        telemetry.addData("  • M", "%.2f A", midAmps);
        telemetry.addData("  • R", "%.2f A", rightAmps);

        telemetry.addLine("HUBS ==========");
        for (LynxModule hub : allHubs) {
            // Battery voltage (12V input from XT30 connector)
            double voltage = hub.getInputVoltage(VoltageUnit.VOLTS);

            // Total current draw for this hub
            double totalCurrent = hub.getCurrent(CurrentUnit.AMPS);

            // Breakdown by bus
            double gpioCurrent = hub.getGpioBusCurrent(CurrentUnit.MILLIAMPS);
            double i2cCurrent = hub.getI2cBusCurrent(CurrentUnit.MILLIAMPS);

            telemetry.addData("  Hub", hub.getDeviceName());
            telemetry.addData("    • Voltage", "%.2f V", voltage);
            telemetry.addData("    • Total Current", "%.2f A", totalCurrent);
            telemetry.addData("    • GPIO Bus", "%.1f mA", gpioCurrent);
            telemetry.addData("    • I2C Bus", "%.1f mA", i2cCurrent);
        }

        telemetry.addLine("INFO ==========");
        telemetry.addData("  • Motor Info", motorInfo);

    }

}

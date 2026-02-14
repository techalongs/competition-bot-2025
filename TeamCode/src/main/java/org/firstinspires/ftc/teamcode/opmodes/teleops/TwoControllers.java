package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.controls.AnnaControls;
import org.firstinspires.ftc.teamcode.opmodes.controls.OtherControls;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.controls.GamepadControls;
import org.firstinspires.ftc.teamcode.util.LogitechCamera;
import org.firstinspires.ftc.teamcode.util.REVColorSensor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@TeleOp(name = "Two Controller TeleOp - PS4", group = "Normal Controls")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private CommandScheduler commandScheduler;
    private ToggleButtonReader toggleDriveSlow;
    private ToggleButtonReader toggleFieldCentric;
    private REVColorSensor sensor1;
    private REVColorSensor sensor2;
    private LogitechCamera camera;
    private double driveFastSpeedLimit = 1.0;
    private double driveSlowSpeedLimit = 0.5;
    private GamepadControls gamepadControls;
    // ------------------------------ //
    double forward, strafe, rotate;
    double kP = 0.019;
    double error = 0;
    double lastError = 0;
    double goalX = 0; // offset here
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double curTime = 0;
    double lastTime = 0;
    double[] stepSizes = {0.1, 0.001, 0.0001};
    int stepIndex = 1;

    @Override
    public void init() {
        sensor1 = new REVColorSensor(hardwareMap, "rightSensor1");
        sensor2 = new REVColorSensor(hardwareMap, "rightSensor2");

        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        commandScheduler = CommandScheduler.getInstance();

        gamepadControls = new AnnaControls(driver1, driver2, robot);
        toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
        toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();

        driver2.getGamepadButton(GamepadKeys.Button.OPTIONS).and(driver2.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)).toggleWhenActive(() -> {
                    commandScheduler.clearButtons();
                    gamepadControls = new OtherControls(driver1, driver2, robot);
                    toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
                    toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();
                },
                () -> {
                    commandScheduler.clearButtons();
                    gamepadControls = new AnnaControls(driver1, driver2, robot);
                    toggleDriveSlow = gamepadControls.getDriveSlowToggleReader();
                    toggleFieldCentric = gamepadControls.getFieldCentricToggleReader();
                });
    }

    public void start() {
        resetRuntime();
        curTime = getRuntime();
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
        telemetry.addData("Launcher Power State", gamepadControls.getLauncherPower().name());
        telemetry.addData("Drive state", getDriveState());
        if (!gamepadControls.isDefault()) {
            telemetry.addData("Controls", gamepadControls);
            telemetry.addLine(" Option + Left Bumper or Cross to switch back");
        }

        camera = new LogitechCamera(hardwareMap, "wedcam1");
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData("FPS", camera.visionPortal.getFps());

        // 3. Process Data: Retrieve detections
        List<AprilTagDetection> currentDetections = camera.processor.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
// --- 1. GET POSE VALUES FROM TELEMETRY DATA ---
                double x = detection.ftcPose.x; // Sideways distance in mm
                double y = detection.ftcPose.y; // Forward distance in mm
                double yaw = detection.ftcPose.yaw; // Rotation in degrees

                telemetry.update();
            }
        }
        // ------------- get mecanum drive inputs ---------------
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        // ---------------- get april tag info -----------------
         camera.upadate();
        AprilTagDetection id20 = camera.getTagBySpecificId(20);

        if (gamepad1.left_trigger > 0.3) {
            if (id20 != null) {
                error = goalX - id20.ftcPose.bearing; // tx

                if (Math.abs(error) < angleTolerance) {
                    rotate = 0;
                } else {
                    double pTerm = error * kP;

                    curTime = getRuntime();
                    double dT = curTime - lastTime;
                    double dTerm = ((error - lastError) / dT) * kD;

                    rotate = Range.clip(pTerm + dTerm, -0.4, 0.4);

                    lastError = error;
                    lastTime = curTime;
                }
            } else {
                lastTime = getRuntime();
                lastError = 0;
            }
        } else {
            lastError = 0;
            lastTime = getRuntime();
        }

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

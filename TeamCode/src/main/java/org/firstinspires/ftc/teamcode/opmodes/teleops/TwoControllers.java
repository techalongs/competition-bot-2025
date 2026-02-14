package org.firstinspires.ftc.teamcode.opmodes.teleops;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.opmodes.controls.ControlsV2;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.opmodes.controls.GamepadControls;
import org.firstinspires.ftc.teamcode.util.LogitechCamera;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Arrays;
import java.util.List;

@TeleOp(name = "Two Controller TeleOp", group = "Normal Controls")
public class TwoControllers extends OpMode {

    private GamepadEx driver1;
    private GamepadEx driver2;
    private Robot robot;
    private LogitechCamera camera;
    private GamepadControls gamepadControls;
    // ------------------------------ //
    // Auto Aim
    double forward, strafe, rotate;
    double kP = 0.019;
    double error = 0;
    double lastError = 0;
    double goalX = 0; // offset here
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double currentTime = 0;
    double lastTime = 0;
    double[] stepSizes = {0.1, 0.001, 0.0001};
    int stepIndex = 1;

    @Override
    public void init() {
        this.telemetry = new JoinedTelemetry(this.telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        driver1 = new GamepadEx(gamepad1);
        driver2 = new GamepadEx(gamepad2);
        robot = new Robot(hardwareMap);
        gamepadControls = new ControlsV2(driver1, driver2, robot);
        camera = new LogitechCamera(hardwareMap, "Webcam 1");
    }

    public void start() {
        resetRuntime();
        currentTime = getRuntime();
    }

    @Override
    public void loop() {
        loopReadStuff();

        robot.drive(Drivetrain.DriveState.ROBOT_CENTRIC, driver1, RobotConfig.driveSpeedLimit);
        this.loopAutoAim();

        telemetry.addData("Launcher Colors", Arrays.toString(robot.getLauncherColors()));
        telemetry.addData("Launcher Power State", RobotConfig.launcherPower.name());
        telemetry.addData("Drive Speed", RobotConfig.driveSpeedLimit);
    }

    private void loopAutoAim() {
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
        forward = -driver1.getLeftX();
        strafe = driver1.getLeftX();
        rotate = driver1.getRightX();

        // ---------------- get april tag info -----------------
        camera.update();
        AprilTagDetection id20 = camera.getTagBySpecificId(20);

        if (gamepad1.left_trigger > 0.3) {
            if (id20 != null) {
                error = goalX - id20.ftcPose.bearing; // tx

                if (Math.abs(error) < angleTolerance) {
                    rotate = 0;
                } else {
                    double pTerm = error * kP;

                    currentTime = getRuntime();
                    double dT = currentTime - lastTime;
                    double dTerm = ((error - lastError) / dT) * kD;

                    rotate = Range.clip(pTerm + dTerm, -0.4, 0.4);

                    lastError = error;
                    lastTime = currentTime;
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

    public void loopReadStuff() {
        CommandScheduler.getInstance().run();
        driver1.readButtons();
        driver2.readButtons();
    }

}

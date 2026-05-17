package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebcamVision extends SubsystemBase {

    public final AprilTagProcessor processor;
    public final VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    private boolean liveViewEnabled = false;

    public WebcamVision(HardwareMap hardwareMap, String cameraId) {
        processor = new AprilTagProcessor.Builder()
                .setTagLibrary(AprilTagGameDatabase.getDecodeTagLibrary())
                .setDrawAxes(liveViewEnabled)
                .setDrawCubeProjection(liveViewEnabled)
                .setDrawTagOutline(liveViewEnabled)
                .setDrawTagID(liveViewEnabled)
                .setNumThreads(1)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setLensIntrinsics(
                        RobotConfig.webcamLensIntrinsicsFx1920x1080,
                        RobotConfig.webcamLensIntrinsicsFy1920x1080,
                        RobotConfig.webcamLensIntrinsicsCx1920x1080,
                        RobotConfig.webcamLensIntrinsicsCy1920x1080)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, cameraId))
                .setCameraResolution(RobotConfig.webcamResolution)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setShowStatsOverlay(liveViewEnabled)
                .addProcessor(processor)
                .build();

        this.setManualExposure(5, 255);
    }

    @Override
    public void periodic() {
    }

    public void start() {
        visionPortal.resumeStreaming();
        if (liveViewEnabled) {
            visionPortal.resumeLiveView();
        }
    }

    public void stop() {
        visionPortal.stopStreaming();
        visionPortal.close();
    }

    private void initVision() {
            VisionPortal.Builder builder = new VisionPortal.Builder();
            builder.setCamera(cameraName);
            builder.setShowStatsOverlay(liveViewEnabled);
//            builder.setAutoStartStreamOnBuild(true);

            // Choose a camera resolution. Not all cameras support all resolutions.
            //builder.setCameraResolution(new Size(640, 480));
            builder.setCameraResolution(resolution);

            // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
            builder.enableLiveView(liveViewEnabled);

            // Set the stream format; MJPEG uses less bandwidth than default YUY2.
//            builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
            builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

            // Choose whether or not LiveView stops if no processors are enabled.
            // If set "true", monitor shows solid orange screen if no processors enabled.
            // If set "false", monitor shows camera view without annotations.
//            builder.setAutoStopLiveView(true);

            // Set and enable the processor.
            builder.addProcessor(aprilTagProcessor);

            // Build the Vision Portal, using the above settings.
            visionPortal = builder.build();

            // Disable or re-enable the aprilTag processor at any time.
            visionPortal.setProcessorEnabled(aprilTagProcessor, true);

            this.setManualExposure(6, 20);
    }

    private void setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open and streaming.
        if (visionPortal == null || visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            ctx.tell("Camera", "Waiting...");
            // This loop will halt the init process until the camera is ready.
            while (visionPortal != null && visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
            }
            ctx.tell("Camera", "Ready");
        }

        // Set camera controls.
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            try {
                // Get the ExposureControl and GainControl
                ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
                GainControl gainControl = visionPortal.getCameraControl(GainControl.class);

                // Set the exposure mode to Manual
                if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                    exposureControl.setMode(ExposureControl.Mode.Manual);
                    Thread.sleep(50); // Give the camera time to switch modes
                }

                // Set the exposure and gain
                exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
                Thread.sleep(20);
                gainControl.setGain(gain);
                Thread.sleep(20);

            } catch (Exception e) {
                // Handle exceptions, which might occur if the camera is unplugged
                // or the controls are not supported on this camera.
                ctx.tell("Camera Control Error", e.getMessage());
            }
        }
    }

    public AprilTagDetection getTagBySpecificId(int id) {
        for (AprilTagDetection detection : detectedTags) {
            if (detection.id == id){
                return detection;
            }
        }
        return null;
    }

}

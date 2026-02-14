package org.firstinspires.ftc.teamcode.util;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogitechCamera implements HardwareDevice {
    public final AprilTagProcessor processor;
    public final VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public LogitechCamera(HardwareMap hardwareMap, String id) {
        processor = new AprilTagProcessor.Builder()
                .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setLensIntrinsics(1440.381342, 1435.602662, 959.8105025, 525.3844601)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, id))
                .setCameraResolution(new Size(1920, 1080))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(processor)
                .build();

        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {}

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setMode(ExposureControl.Mode.Manual);
        exposureControl.setExposure(5, TimeUnit.MILLISECONDS); // Minimize

        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(255); // Maximize
    }

    public List<AprilTagDetection> getDetections() {
        return processor.getDetections();
    }

    public void update() {
        detectedTags = processor.getDetections();
    }

    public AprilTagDetection getTagBySpecificId(int id) {
        for (AprilTagDetection detection : detectedTags) {
            if (detection.id == id){
                return detection;
            }
        }
        return null;
    }



    @Override
    public void disable() {
        visionPortal.close();
    }

    @Override
    public String getDeviceType() {
        return "Logitech C920 Webcam";
    }
}

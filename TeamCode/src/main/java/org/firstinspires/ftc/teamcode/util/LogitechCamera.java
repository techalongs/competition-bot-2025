package org.firstinspires.ftc.teamcode.util;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class LogitechCamera implements HardwareDevice {
    private final AprilTagProcessor processor;
    private final VisionPortal visionPortal;

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
    }

    public List<AprilTagDetection> getDetections() {
        return processor.getDetections();
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

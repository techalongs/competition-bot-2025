package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

public class SnowdogLens extends SubsystemBase {

    private final int READ_PERIOD = 1;

    private final HuskyLens huskyLens;
    private final Deadline rateLimit;
    private final Telemetry telemetry;
    private final CameraName cameraName;

    public SnowdogLens(Telemetry telemetry, HardwareMap hardwareMap) {

        try {
            huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");
            cameraName = hardwareMap.get(WebcamName.class, "huskyLens");
        } catch (Throwable t) {
            throw new RuntimeException("Error creating Husky Lens: " + t.getMessage(), t);
        }

        this.telemetry = telemetry;

        rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
        rateLimit.expire();
    }

    public void init() {
        if (!huskyLens.knock()) {
            telemetry.addData("HuskyLens: Error", "Error communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData("HuskyLens: Status", "Ready");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        rateLimit.expire();
    }

    public void loop() {
                telemetry.addData("Ctx: Rate Limit Expired", rateLimit.hasExpired());
            rateLimit.reset();

            HuskyLens.Block[] blocks = huskyLens.blocks();
            telemetry.addData("HL: Block Count", blocks.length);
            for (HuskyLens.Block block : blocks) {
                telemetry.addData("HL: Block", block.toString());

            }
    }
}

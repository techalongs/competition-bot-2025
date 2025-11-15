package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeamUtil {

    public static void debug(Telemetry telemetry, String name, String value) {
        telemetry.addData(name, value);
        telemetry.update();
        try {
            Thread.sleep(3000);
        } catch (Throwable t) {
            // Ignore
        }
    }
}

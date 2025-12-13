package org.firstinspires.ftc.teamcode.util;

import com.seattlesolvers.solverslib.hardware.HardwareDevice;

public class LogitechCamera implements HardwareDevice {
    // TODO: Make a WebcamName variable
    // TODO: Research and re-read camera intro

    @Override
    public void disable() {

    }

    @Override
    public String getDeviceType() {
        return "Logitech C920 Webcam";
    }
}

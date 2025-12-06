package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.util.REVColorSensor;

public class Launcher extends SubsystemBase {
    private final MotorEx launcher;
    private final REVColorSensor sensor;

    public enum Color {
        GREEN(new float[] {60, 190}),
        PURPLE(new float[] {190, 360});

        final float[] range;
        Color (float[] range) {
            this.range = range;
        }
    }

    public Launcher(HardwareMap hardwareMap, String launch, String sensor, boolean inverted) {
        this.launcher = new MotorEx(hardwareMap, launch);
        this.sensor = new REVColorSensor(hardwareMap, sensor);

        launcher.setInverted(inverted);
        launcher.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        launcher.stopAndResetEncoder();
        launcher.setRunMode(MotorEx.RunMode.RawPower);
    }

    public Color getColor() {
        float hue = sensor.RGBtoHSV(sensor.red(), sensor.green(), sensor.blue(), new float[3])[0];
        if (Color.GREEN.range[0] < hue && hue < Color.GREEN.range[1]) return Color.GREEN;
        else if (Color.PURPLE.range[0] < hue && hue < Color.PURPLE.range[1]) return Color.PURPLE;
        return null;
    }

    public void launch() {
        launcher.set(1);
    }

    public void reloadSlow() {
        launcher.set(-0.5);
    }

    public void reload() {
        launcher.set(-1);
    }

    public void stopLauncher() {
        launcher.set(0);
    }
}

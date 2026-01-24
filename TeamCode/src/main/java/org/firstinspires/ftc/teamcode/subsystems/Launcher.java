package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import org.firstinspires.ftc.teamcode.util.REVColorSensor;

public class Launcher extends SubsystemBase {
    private final MotorEx launcher;
    private final REVColorSensor sensor1;
    private final REVColorSensor sensor2;

    public enum Color {
        GREEN(new float[] {60, 190}),
        PURPLE(new float[] {190, 360});

        final float[] range;
        Color (float[] range) {
            this.range = range;
        }
    }

    public enum Power {
        LONG(1),
        MID(0.8),
        SHORT(0.7);

        final double power;
        Power(double power) {
            this.power = power;
        }
    }

    public Launcher(HardwareMap hardwareMap, String launch, String sensor, boolean inverted) {
        this.launcher = new MotorEx(hardwareMap, launch);
        this.sensor1 = new REVColorSensor(hardwareMap, sensor + "1");
        this.sensor2 = new REVColorSensor(hardwareMap, sensor + "2");

        launcher.setInverted(inverted);
        launcher.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        launcher.stopAndResetEncoder();
        launcher.setRunMode(MotorEx.RunMode.RawPower);
    }

    private boolean inRange(Color color, float hue) {
        return color.range[0] < hue && hue < color.range[1];
    }

    public Color getColor() {
        float hue1 = sensor1.RGBtoHSV(sensor1.red(), sensor1.green(), sensor1.blue(), new float[3])[0];
        float hue2 = sensor2.RGBtoHSV(sensor2.red(), sensor2.green(), sensor2.blue(), new float[3])[0];
        if (inRange(Color.PURPLE, hue1) || inRange(Color.PURPLE, hue2)) return Color.PURPLE;
        else if (inRange(Color.GREEN, hue1) || inRange(Color.GREEN, hue2)) return Color.GREEN;
        return null;
    }

    public void launch(Power power) {
        launcher.set(power.power);
    }

    public void reload() {
        launcher.set(-1);
    }

    public void stopLauncher() {
        launcher.set(0);
    }
}

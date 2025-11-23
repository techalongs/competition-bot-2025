package org.firstinspires.ftc.teamcode.util;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.seattlesolvers.solverslib.hardware.HardwareDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import lombok.Getter;

/**
 * An extended wrapper class for the REV ColorSensor v3
 *
 * @author Arush - FTC 23511
 */

public class REVColorSensor implements HardwareDevice {
    /**
     * -- GETTER --
     *  Gets the underlying NormalizedColorSensor object
     */
    @Getter
    private final NormalizedColorSensor colorSensor;
    private DistanceUnit distanceUnit;

    /**
     * Constructs a color sensor, defaults to ARGB
     */
    public REVColorSensor(NormalizedColorSensor colorSensorV3) {
        this.colorSensor = colorSensorV3;
    }

    /**
     * Constructs a color sensor, defaults to ARGB
     */
    public REVColorSensor(NormalizedColorSensor colorSensorV3, DistanceUnit distanceUnit) {
        this(colorSensorV3);
        this.distanceUnit = distanceUnit;
    }

    /**
     * Constructs a color sensor using the given hardware map and name, defaults to ARGB
     */
    public REVColorSensor(@NonNull HardwareMap hardwareMap, String name) {
        this((NormalizedColorSensor) hardwareMap.colorSensor.get(name));
    }

    /**
     * Constructs a color sensor using the given hardware map and name, defaults to ARGB
     */
    public REVColorSensor(@NonNull HardwareMap hardwareMap, String name, DistanceUnit distanceUnit) {
        this((NormalizedColorSensor) hardwareMap.colorSensor.get(name));
        this.distanceUnit = distanceUnit;
    }

    /**
     * Convert HSV value to an ARGB one. Includes alpha.
     *
     * @return an int representing the ARGB values
     */
    public int[] HSVtoARGB(int alpha, float[] hsv) {
        int color = Color.HSVToColor(alpha, hsv);
        return new int[]{Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)};
    }

    /**
     * Converts an RGB value to an HSV value. Provide the float[] to be used.
     */
    public float[] RGBtoHSV(int red, int green, int blue, float[] hsv) {
        Color.RGBToHSV(red, green, blue, hsv);
        return hsv;
    }

    /**
     * Get all the ARGB values in an array from the sensor
     *
     * @return an int array representing ARGB
     */
    public float[] getARGB() {
        return new float[]{ alpha(), red(), green(), blue() };
    }

    /**
     * Gets the alpha value from the sensor
     */
    public float alpha() {
        return colorSensor.getNormalizedColors().alpha;
    }

    /**
     * Gets the red value from the sensor
     */
    public int red() {
        return (int) (colorSensor.getNormalizedColors().red * 255);
    }

    /**
     * Gets the green value from the sensor
     */
    public int green() {
        return (int) (colorSensor.getNormalizedColors().green * 255);
    }

    /**
     * Gets the blue value from the sensor
     */
    public int blue() {
        return (int) (colorSensor.getNormalizedColors().blue * 255);
    }

    @Override
    public void disable() {
        colorSensor.close();
    }

    @Override
    public String getDeviceType() {
        return "REV Color Sensor v3";
    }
}

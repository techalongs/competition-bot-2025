package org.firstinspires.ftc.teamcode.util;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lombok.Getter;

/**
 * Rev Robotics potentiometer.
 * Note: when adding to the control hub configuration, the type should be "Analog Input".
 *
 * Docs:
 * https://docs.revrobotics.com/rev-crossover-products/sensors/potentiometer/specifications
 */
@Getter
public class RevPotentiometer {

    private final String id;
    private final AnalogInput input;

    public RevPotentiometer(HardwareMap hardwareMap, String id) {
        this.id = id;
        input = hardwareMap.get(AnalogInput.class, id);
    }

    public double getVoltage() {
        return input.getVoltage();
    }

    /**
     * Gets the degrees of rotation.
     * Warning: Rev docs state this may be unreliable.
     *
     * @return Degrees of rotation
     */
    public double getDegrees() {
        return this.voltageToDegrees(this.getVoltage());
    }

    /**
     * Convert the output voltage to degrees (0-270), though Rev warns this may be unreliable
     * due to Control Hub conversion.
     *
     * @param voltage Output voltage from the pot
     * @return Degrees (0-270)
     */
    private double voltageToDegrees(double voltage) {
        if (Math.abs(voltage) < 0.001) {
            return 270.0;
        }

        double b = -(270.0 * voltage + 445.5);
        double c = 120285.0 - 36450.0 * voltage;

        double discriminant = 218700.0 * voltage * voltage - 240570.0 * voltage + 198470.25;

        if (discriminant < 0) {
            return Double.NaN;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double theta1 = (-b + sqrtDiscriminant) / (2.0 * voltage);
        double theta2 = (-b - sqrtDiscriminant) / (2.0 * voltage);

        if (theta2 >= 0 && theta2 <= 270) {
            return theta2;
        } else if (theta1 >= 0 && theta1 <= 270) {
            return theta1;
        }

        return Double.NaN;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Pot#%s: %0.2f", id, this.getVoltage());
    }

}

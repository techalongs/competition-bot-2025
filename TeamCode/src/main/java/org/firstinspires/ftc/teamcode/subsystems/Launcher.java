package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.teamcode.util.REVColorSensor;
import org.firstinspires.ftc.teamcode.util.RevPotentiometer;
import org.firstinspires.ftc.teamcode.util.SleepCommand;

public class Launcher extends SubsystemBase {
    private final CRServoEx launcher;
    private final RevPotentiometer potentiometer;
    private final REVColorSensor sensor;
    private final double SERVO_TOLERANCE = 0.01;

    private enum Position {
        LOAD(0.2),
        READY(0.75),
        FIRE(0.95);

        final double val;

        Position(double v) {
            this.val = v;
        }
    }

    public enum Color {
        GREEN(new float[] {60, 190}),
        PURPLE(new float[] {190, 360});

        final float[] range;
        Color (float[] range) {
            this.range = range;
        }
    }

    private class MoveTo extends CommandBase {
        Position pos;

        MoveTo(Position pos) {
            this.pos = pos;
            addRequirements(Launcher.this);
        }

        @Override
        public void initialize() {
            if (potentiometer.getVoltage() > pos.val) launcher.set(1);
            else launcher.set(-1);
        }

        @Override
        public boolean isFinished() {
            return inRange(pos.val);
        }

        @Override
        public void end(boolean interrupted) {
            launcher.set(0);
        }
    }

    public Launcher(HardwareMap hardwareMap, String launch, String pot, String sensor, boolean inverted) {
        this.launcher = new CRServoEx(hardwareMap, launch);
        this.potentiometer = new RevPotentiometer(hardwareMap, pot);
        this.sensor = new REVColorSensor(hardwareMap, sensor);

        launcher.setInverted(inverted);
        launcher.setRunMode(CRServoEx.RunMode.RawPower);
    }

    private boolean inRange(double value) {
        return value - SERVO_TOLERANCE < potentiometer.getVoltage()
                && potentiometer.getVoltage() < value + SERVO_TOLERANCE;
    }

    public Color getColor() {
        float hue = sensor.RGBtoHSV(sensor.red(), sensor.green(), sensor.blue(), new float[3])[0];
        if (Color.GREEN.range[0] < hue && hue < Color.GREEN.range[1]) return Color.GREEN;
        else return Color.PURPLE;
    }

    public Command launch() {
        return new SequentialCommandGroup(
                new MoveTo(Position.FIRE),
                new SleepCommand(250),
                new MoveTo(Position.LOAD),
                new SleepCommand(250),
                new MoveTo(Position.READY)
        );
    }
}

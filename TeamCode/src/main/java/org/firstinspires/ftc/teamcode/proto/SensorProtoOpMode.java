package org.firstinspires.ftc.teamcode.proto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.RevPotentiometer;

@TeleOp(name = "Proto: Sensors", group = "Proto")
public class SensorProtoOpMode extends OpMode {

    private RevPotentiometer pot1;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        pot1 = new RevPotentiometer(hardwareMap, "pot1");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.addData("Status", "Running");
        telemetry.addLine("Pot 1 ----------");
        telemetry.addData("Volts", pot1.getVoltage());
        telemetry.addData("Degrees", pot1.getDegrees());
        telemetry.update();

    }

}

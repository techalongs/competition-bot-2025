package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Intake extends SubsystemBase {

    // Motors and stuff
    // 1. Grabber
    // 2. Elevator/Lifter
    // 3. Bubbler

    public void startGrabber() {
    }

    public void stopGrabber() {

    }

    public void raiseLifter() {

    }

    public void lowerLifter() {

    }

    public void startBubbler() {

    }

    public void stopBubbler() {

    }

    public void startElevator() {
        this.startBubbler();
        this.raiseLifter();
    }

}

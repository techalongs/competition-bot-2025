package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.geometry.Pose;

public enum RedPosition {
    SHORT_START(new Pose(126, 121, Math.toRadians(40))),
    LONG_START(new Pose(88, 8, Math.toRadians(90))),
    SHORT_SHOOT(new Pose(95, 95, Math.toRadians(45))),
    LONG_SHOOT(new Pose(56, 36, Math.toRadians(67))),
    SHORT_COLLECT_PREP(new Pose(95, 82, Math.toRadians(0))),
    SHORT_COLLECT(new Pose(120, 82, Math.toRadians(0))),
    MID_COLLECT_PREP(new Pose(95, 57, Math.toRadians(0))),
    MID_COLLECT(new Pose(118, 57, Math.toRadians(0))),
    LONG_COLLECT_PREP(new Pose(95, 33, Math.toRadians(0))),
    LONG_COLLECT(new Pose(120, 33, Math.toRadians(0))),
    DUMP_PREP(new Pose(118, 77, Math.toRadians(90))),
    DUMP(new Pose(128, 77, Math.toRadians(90))),
    SHORT_END(new Pose(112, 85, Math.toRadians(0))),
    LONG_END(new Pose(105, 16, Math.toRadians(0)));

    public final Pose pos;
    RedPosition(Pose pos) {
        this.pos = pos;
    }
}

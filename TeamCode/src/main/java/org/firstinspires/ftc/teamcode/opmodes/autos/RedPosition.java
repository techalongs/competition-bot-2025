package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.geometry.Pose;

public enum RedPosition {
    SHORT_START(new Pose(126, 120, Math.toRadians(36))),
    LONG_START(new Pose(88, 8, Math.toRadians(90))),
    SHORT_SHOOT(new Pose(100, 100, Math.toRadians(45))),
    LONG_SHOOT(new Pose(56, 36, Math.toRadians(67))),
    SHORT_COLLECT_PREP(new Pose(100, 83.5, Math.toRadians(0))),
    SHORT_COLLECT(new Pose(125, 83.5, Math.toRadians(0))),
    MID_COLLECT_PREP(new Pose(100, 59.5, Math.toRadians(0))),
    MID_COLLECT(new Pose(125, 59.5, Math.toRadians(0))),
    LONG_COLLECT_PREP(new Pose(100, 35, Math.toRadians(0))),
    LONG_COLLECT(new Pose(125, 35, Math.toRadians(0))),
    DUMP_PREP(new Pose(123, 73, Math.toRadians(90))),
    DUMP(new Pose(128, 73, Math.toRadians(90))),
    SHORT_END(new Pose(112, 85, Math.toRadians(0))),
    LONG_END(new Pose(105, 16, Math.toRadians(0)));

    public final Pose pos;
    RedPosition(Pose pos) {
        this.pos = pos;
    }
}

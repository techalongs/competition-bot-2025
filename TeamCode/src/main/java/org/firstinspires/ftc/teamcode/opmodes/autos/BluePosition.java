package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.geometry.Pose;

public enum BluePosition {
    SHORT_START(new Pose(18, 120, Math.toRadians(140))),
    LONG_START(new Pose(56, 8, Math.toRadians(90))),
    SHORT_SHOOT(new Pose(49, 95, Math.toRadians(135))),
    LONG_SHOOT(new Pose(56, 36, Math.toRadians(67))),
    SHORT_COLLECT_PREP(new Pose(47, 83.5, Math.toRadians(180))),
    SHORT_COLLECT(new Pose(17, 83.5, Math.toRadians(180))),
    MID_COLLECT_PREP(new Pose(45, 60, Math.toRadians(180))),
    MID_COLLECT(new Pose(16, 60, Math.toRadians(180))),
    LONG_COLLECT_PREP(new Pose(49, 38, Math.toRadians(180))),
    LONG_COLLECT(new Pose(24, 38, Math.toRadians(180))),
    DUMP_PREP(new Pose(36, 73, Math.toRadians(90))),
    DUMP(new Pose(16, 68, Math.toRadians(90))),
    SHORT_END(new Pose(32, 85, Math.toRadians(0))),
    LONG_END(new Pose(39, 16, Math.toRadians(0)));

    public final Pose pos;
    BluePosition(Pose pos) {
        this.pos = pos;
    }
}

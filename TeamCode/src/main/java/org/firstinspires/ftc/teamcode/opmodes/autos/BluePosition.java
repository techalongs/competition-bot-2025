package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.geometry.Pose;

public enum BluePosition {
    SHORT_START(new Pose(18, 120, Math.toRadians(140))),
    LONG_START(new Pose(56, 8, Math.toRadians(90))),
    SHORT_SHOOT(new Pose(49, 95, Math.toRadians(135))),
    LONG_SHOOT(new Pose(61, 16, Math.toRadians(113))),
    SHORT_COLLECT_PREP(new Pose(44, 83.5, Math.toRadians(180))),
    SHORT_COLLECT(new Pose(13, 83.5, Math.toRadians(180))),
    MID_COLLECT_PREP(new Pose(44, 55, Math.toRadians(180))),
    MID_COLLECT(new Pose(13, 55, Math.toRadians(180))),
    LONG_COLLECT_PREP(new Pose(44, 35, Math.toRadians(180))),
    LONG_COLLECT(new Pose(13, 35, Math.toRadians(180))),
    DUMP_PREP(new Pose(21, 80, Math.toRadians(270))),
    DUMP(new Pose(16, 80, Math.toRadians(270))),
    SHORT_END(new Pose(29, 85, Math.toRadians(180))),
    LONG_END(new Pose(30, 20, Math.toRadians(180)));

    public final Pose pos;
    BluePosition(Pose pos) {
        this.pos = pos;
    }
}

package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.geometry.Pose;

public enum BluePosition {
    SHORT_START(new Pose(18, 120, Math.toRadians(36))),
    LONG_START(new Pose(56, 8, Math.toRadians(90))),
    SHOOT(new Pose(45, 100, Math.toRadians(135))),
    SHORT_COLLECT_PREP(new Pose(44, 83.5, Math.toRadians(180))),
    SHORT_COLLECT(new Pose(19, 83.5, Math.toRadians(180))),
    MID_COLLECT_PREP(new Pose(44, 59.5, Math.toRadians(180))),
    MID_COLLECT(new Pose(19, 59.5, Math.toRadians(180))),
    LONG_COLLECT_PREP(new Pose(44, 35, Math.toRadians(180))),
    LONG_COLLECT(new Pose(19, 35, Math.toRadians(180))),
    DUMP_PREP(new Pose(21, 73, Math.toRadians(90))),
    DUMP(new Pose(16, 73, Math.toRadians(90))),
    SHORT_END(new Pose(32, 85, Math.toRadians(180))),
    LONG_END(new Pose(39, 16, Math.toRadians(180)));

    public final Pose pos;
    BluePosition(Pose pos) {
        this.pos = pos;
    }
}

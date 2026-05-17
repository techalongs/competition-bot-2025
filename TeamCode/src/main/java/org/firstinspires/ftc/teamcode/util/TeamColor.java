package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;

/**
 * Enumeration of team colors: Blue or Red. Includes meta information relating to the game, like
 * positions and angles for localization and targeting.
 */
public enum TeamColor {

    BLUE(20),
    RED(24);

    public final int targetTagId;
    public final double targetTagFieldPositionX;
    public final double targetTagFieldPositionY;
    public final double targetTagFieldPositionZ;
    public final DistanceUnit targetTagDistanceUnit;
    public final AprilTagMetadata targetTagMetadata;

    /**
     *
     */
    TeamColor(int targetTagId) {
        targetTagMetadata = AprilTagGameDatabase.getDecodeTagLibrary().lookupTag(targetTagId);
        assert targetTagMetadata != null : "AprilTag ID " + targetTagId + " not found!";
        this.targetTagId = targetTagId;
        targetTagFieldPositionX = targetTagMetadata.fieldPosition.get(0);
        targetTagFieldPositionY = targetTagMetadata.fieldPosition.get(1);
        targetTagFieldPositionZ = targetTagMetadata.fieldPosition.get(2);
        targetTagDistanceUnit = targetTagMetadata.distanceUnit;
    }
}

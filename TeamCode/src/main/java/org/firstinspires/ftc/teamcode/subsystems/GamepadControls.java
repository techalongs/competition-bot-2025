package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

public interface GamepadControls {

    public ToggleButtonReader getDriveSlowToggleReader();
    public ToggleButtonReader getFieldCentricToggleReader();
    default boolean isDefault() {
        return true;
    }
}

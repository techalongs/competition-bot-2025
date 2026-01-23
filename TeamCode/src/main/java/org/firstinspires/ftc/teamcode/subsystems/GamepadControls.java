package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

public interface GamepadControls {

    ToggleButtonReader getDriveSlowToggleReader();

    ToggleButtonReader getFieldCentricToggleReader();

    Launcher.Power getLauncherPower();

    default boolean isDefault() {
        return true;
    }

}

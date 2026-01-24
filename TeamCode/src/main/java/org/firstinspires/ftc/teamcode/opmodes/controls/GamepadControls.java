package org.firstinspires.ftc.teamcode.opmodes.controls;

import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public interface GamepadControls {

    ToggleButtonReader getDriveSlowToggleReader();

    ToggleButtonReader getFieldCentricToggleReader();

    Launcher.Power getLauncherPower();

    default boolean isDefault() {
        return true;
    }

}

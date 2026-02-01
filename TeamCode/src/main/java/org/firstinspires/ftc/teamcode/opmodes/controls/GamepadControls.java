package org.firstinspires.ftc.teamcode.opmodes.controls;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.ToggleButtonReader;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.HashMap;

public interface GamepadControls {

    Launcher.Power[] launcherPowers = new Launcher.Power[]{Launcher.Power.LONG, Launcher.Power.MID, Launcher.Power.SHORT};

    ToggleButtonReader getDriveSlowToggleReader();

    ToggleButtonReader getFieldCentricToggleReader();

    Launcher.Power getLauncherPower();

    default boolean isDefault() {
        return false;
    }

    default void setGamepadColors(GamepadEx gamepad, Launcher.Power power) {
        int[] colors = RobotConfig.gamepadColors.get(power);
        assert colors != null;
        gamepad.gamepad.setLedColor(colors[0], colors[1], colors[2], 5000);
    }

}

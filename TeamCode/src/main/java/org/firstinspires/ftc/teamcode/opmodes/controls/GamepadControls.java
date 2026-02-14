package org.firstinspires.ftc.teamcode.opmodes.controls;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.RobotConfig;
import org.firstinspires.ftc.teamcode.subsystems.Launcher;

public interface GamepadControls {

    default void setGamepadColors(GamepadEx gamepad, Launcher.Power power) {
        int[] colors = RobotConfig.gamepadColors.get(power);
        assert colors != null;
        gamepad.gamepad.setLedColor(colors[0], colors[1], colors[2], 5000);
    }

}

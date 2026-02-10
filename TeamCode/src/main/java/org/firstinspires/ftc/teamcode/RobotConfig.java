package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.HashMap;

import lombok.Getter;

/**
 * Robot configuration. This is configurable so it can also be managed via Panels.
 * Note: Static fields require their own @Getter annotation separate from @Data.
 */
@Configurable
public class RobotConfig {

    // Launch Powers
    public static volatile double launchPowerShort = 0.8;
    public static volatile double launchPowerMid = 1;
    public static volatile double launchPowerLong = 1;
    // Default is Short -- Change BOTH of these if you change the default
    @Getter
    public static volatile double launchRawPower = launchPowerShort;
    public static volatile Launcher.Power launcherPower = Launcher.Power.SHORT;

    // Launcher Timing
    // Sleep after hammer swings back for reload, before launch
    public static volatile int sleepBeforeLaunch = 380;
    // Sleep after hammer hits the artifact
    public static volatile int sleepAfterLaunch = 210;

    // Parallel Timing
    // ----------
    // These are described with left/middle/right but they can be used for any sequence.
    // Because you may want to alter the fire sequence based on color detection.
    // These values should be the number of milliseconds after the previous launcher, not after the first launcher.
    // ----------
    // Sleep after first (left) launcher and before second (mid)
    public static volatile int sleepBeforeSecondParallelLauncher = 150;
    // Sleep after second (mid) launcher and before third (right)
    public static volatile int sleepBeforeThirdParallelLauncher = 150;

    // These can be safely ignored unless you want to try different ideas
    public static volatile double reloadPower = -0.7;
    public static volatile int sleepBeforeReload = 0;
    public static volatile int sleepBetweenSequentialLaunches = 0;

    public static volatile double driveFastSpeedLimit = 1.0;
    public static volatile double driveSlowSpeedLimit = 0.5;
    public static volatile double driveSpeedLimit = driveFastSpeedLimit;

    public static HashMap<Launcher.Power, int[]> gamepadColors = new HashMap<Launcher.Power, int[]>() {{
        put(Launcher.Power.SHORT, new int[]{0,255,0});
        put(Launcher.Power.MID, new int[]{0,0,255});
        put(Launcher.Power.LONG, new int[]{255,0,0});
    }};

    public static void setLauncherPower(Launcher.Power power) {
        launcherPower = power;
        launchRawPower = power.power();
    }

}

package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.subsystems.Launcher;

import java.util.HashMap;

/**
 * Robot configuration. This is configurable so it can also be managed via Panels.
 * Note: Static fields require their own @Getter annotation separate from @Data.
 */
@Configurable
public class RobotConfig {

    // Launch Powers
    public static volatile Launcher.Power launcherPower = Launcher.Power.SHORT;
    public static volatile double launchPowerShort = Launcher.Power.SHORT.power();
    public static volatile double launchPowerMid = Launcher.Power.MID.power();
    public static volatile double launchPowerLong = Launcher.Power.LONG.power();
    public static volatile double launchRawPower = Launcher.Power.SHORT.power();


    // Launcher Timing
    // Sleep after hammer swings back for reload, before launch
    public static volatile int sleepBeforeLaunch = 130;
    // Sleep after hammer hits the artifact
    public static volatile int sleepAfterLaunch = 210;

    // Parallel Timing
    // ----------
    // These are described with left/middle/right but they can be used for any sequence.
    // Because you may want to alter the fire sequence based on color detection.
    // These values should be the number of milliseconds after the previous launcher, not after the first launcher.
    // ----------
    // Sleep after first (left) launcher and before second (mid)
    public static volatile int sleepBeforeSecondParallelLauncher = 100;
    // Sleep after second (mid) launcher and before third (right)
    public static volatile int sleepBeforeThirdParallelLauncher = 100;

    // These can be safely ignored unless you want to try different ideas
    public static volatile double reloadPower = -1.0;
    public static volatile int sleepBeforeReload = 0;
    public static volatile int sleepBetweenSequentialLaunches = 0;

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

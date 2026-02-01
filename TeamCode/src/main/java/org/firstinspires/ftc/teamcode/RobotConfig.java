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
    public static Launcher.Power launcherPower = Launcher.Power.SHORT;
    public static double launchPowerShort = Launcher.Power.SHORT.power();
    public static double launchPowerMid = Launcher.Power.MID.power();
    public static double launchPowerLong = Launcher.Power.LONG.power();
    public static double launchRawPower = Launcher.Power.SHORT.power();


    // Launcher Timing
    // Sleep after hammer swings back for reload, before launch
    public static int sleepBeforeLaunch = 130;
    // Sleep after hammer hits the artifact
    public static int sleepAfterLaunch = 210;

    // Parallel Timing
    // ----------
    // These are described with left/middle/right but they can be used for any sequence.
    // Because you may want to alter the fire sequence based on color detection.
    // These values should be the number of milliseconds after the previous launcher, not after the first launcher.
    // ----------
    // Sleep after first (left) launcher and before second (mid)
    public static int sleepBeforeSecondParallelLauncher = 100;
    // Sleep after second (mid) launcher and before third (right)
    public static int sleepBeforeThirdParallelLauncher = 100;

    // These can be safely ignored unless you want to try different ideas
    public static double reloadPower = -1.0;
    public static int sleepBeforeReload = 0;
    public static int sleepBetweenSequentialLaunches = 0;

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

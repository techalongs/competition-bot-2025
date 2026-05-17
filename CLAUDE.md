# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FTC (FIRST Tech Challenge) competition robot code for the Techalongs team (2024-2025 season). Built on the FTC Robot Controller SDK as an Android Gradle project targeting the REV Control Hub.

## Build & Deploy

```bash
# Build the project (Android Gradle)
./gradlew assembleDebug

# Build just TeamCode module
./gradlew :TeamCode:assembleDebug
```

Deployment to the robot is done via Android Studio run configurations:
- **TeamCode**: Full build and deploy to Control Hub
- **SlothLoad**: Fast incremental deploy (only changed code). Requires stop/start of OpMode but not robot restart.

There are no unit tests in this project. Code is tested by deploying to the physical robot.

## Architecture

All team code lives under `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`. The `FtcRobotController/` module is the stock FTC SDK — don't modify it.

### Core Pattern: Command-Based Architecture

Uses **SolversLib** (a fork of FTCLib) for command-based robot programming. Key concepts:
- **Subsystems** (`SubsystemBase`): Hardware abstractions that own motors/servos/sensors
- **Commands**: Actions that use subsystems (InstantCommand, SequentialCommandGroup, ParallelCommandGroup, DeferredCommand)
- **CommandScheduler**: Runs in the OpMode loop, executes queued commands
- **GamepadEx**: Enhanced gamepad with button event detection (whenPressed, toggleWhenActive, etc.)

### Key Classes

- **`Robot`** — Central facade. Constructs all subsystems, exposes high-level Commands (launch, intake, drive). OpModes interact with hardware through this class.
- **`RobotConfig`** — Static configurable parameters (launch powers, timing, drive speed limits, vision intrinsics). Uses `@Configurable` annotation for Panels dashboard tuning at runtime.
- **`GamepadControls`** — Interface for control schemes. Implementations (e.g., `ControlsV2`) bind gamepad buttons to Robot commands.

### Subsystems (`subsystems/`)

| Subsystem | Purpose |
|-----------|---------|
| `Drivetrain` | Mecanum drive with robot-centric and field-centric modes via IMU |
| `Intake` | Motor-driven intake with servo "fork" for sample sorting |
| `Launcher` | Three independent launchers (left/mid/right) with REV color sensors for sample detection. Each has reload→launch→stop sequence. |
| `Lifter` | Dual-motor lift mechanism (currently commented out in Robot) |
| `WebcamVision` | AprilTag detection via VisionPortal with calibrated Logitech camera intrinsics |

### OpModes (`opmodes/`)

- **`teleops/`** — TeleOp modes (TwoControllers, OneController) that wire up controls and run the command loop
- **`autos/`** — Autonomous modes using Pedro Pathing for path following. Four variants: CloseRed, CloseBlue, FarRed, FarBlue
- **`controls/`** — Gamepad button binding implementations (ControlsV2)

### Autonomous Structure

Autos use **Pedro Pathing** for path following with the GoBILDA Pinpoint odometry localizer:
1. Position enums (`RedPosition`, `BluePosition`) define named Poses on the field
2. `AutoCommand` is a `SequentialCommandGroup` that chains `FollowPathCommand`s with robot actions
3. Path constants and follower config live in `pedroPathing/Constants`

### Hardware Name Mapping

Motor/sensor names in code must match the Control Hub's hardware configuration:
- Drive: `frontLeft`, `frontRight`, `backLeft`, `backRight`
- Launchers: `leftLauncher`, `midLauncher`, `rightLauncher`
- Color sensors: `leftSensor1/2`, `midSensor1/2`, `rightSensor1/2`
- Intake: `intakeLift`, `intakeFork`
- IMU: `imu`
- Odometry: `pinpoint`
- Camera: `Webcam 1`

## Key Libraries

- **SolversLib** (`org.solverslib:core:0.3.3`) — Command framework, hardware wrappers, mecanum drive
- **SolversLib Pedro** (`org.solverslib:pedroPathing:0.3.3`) — Pedro Pathing integration commands
- **Pedro Pathing** — Autonomous path following with Bezier curves
- **Lombok** — `@Getter`, `@Data` annotations to reduce boilerplate
- **Sloth** (`dev.frozenmilk.sinister:Sloth`) — Fast incremental code loading
- **Panels/Configurables** (`@Configurable`) — Runtime parameter tuning dashboard

## Conventions

- Java 8 source compatibility (required by FTC SDK)
- OpModes are annotated with `@TeleOp` or `@Autonomous` with name and group
- Launcher colors are GREEN and PURPLE (game-specific sample types)
- Launch power levels: SHORT, MID, LONG — each mapped to a configurable power value
- The `blackboard` is used to pass data (like `TeamColor`) between autonomous and teleop

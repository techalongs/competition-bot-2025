# Dragon Flight and Fire-Breathing System

## Overview

The Dragon system provides flight mechanics and fire-breathing capabilities for the competition bot using SolversLib for hardware control. This implementation is designed to be maintainable, compatible with the FTC framework, and easy to integrate with existing robot code.

## Features

### 1. Flight Mechanics
- **Altitude Control**: Dragon can ascend to a maximum altitude of 100 inches above terrain
- **Wing Flapping**: Realistic wing motion using oscillating motor patterns
- **Hover Control**: Maintains altitude using vertical thrust motor
- **Smooth Takeoff/Landing**: Gradual altitude changes for stable flight

### 2. Fire-Breathing System
- **Configurable Range**: 48-inch fire breath range by default
- **Damage Calculation**: 50 damage units per second
- **Cooldown System**: 3-second cooldown between fire breaths
- **Animation**: Servo-controlled fire breath mechanism with visual effects

### 3. Peasant Targeting
- **Coordinate-Based Targeting**: Target peasants by X/Y coordinates
- **Range Detection**: Automatically checks if target is within fire breath range
- **Head Positioning**: Servo-controlled head aiming system

## Hardware Requirements

### Motors (SolversLib MotorEx)
| Motor Name | Purpose | Recommended Type |
|------------|---------|------------------|
| `dragonLeftWing` | Left wing flapping | GoBILDA 435 RPM |
| `dragonRightWing` | Right wing flapping | GoBILDA 435 RPM |
| `dragonVerticalThrust` | Altitude control | GoBILDA 312 RPM |

### Servos (SolversLib ServoEx)
| Servo Name | Purpose | Range |
|------------|---------|-------|
| `dragonHead` | Targeting head position | 0.0 - 1.0 |
| `dragonFireBreath` | Fire breath animation | 0.0 - 1.0 |

## Configuration

### Robot Configuration File
Add the following hardware devices to your Robot Controller configuration:

```
Motors:
- dragonLeftWing (Motor, Port 0)
- dragonRightWing (Motor, Port 1)
- dragonVerticalThrust (Motor, Port 2)

Servos:
- dragonHead (Servo, Port 0)
- dragonFireBreath (Servo, Port 1)
```

## Usage

### Basic Example

```java
import org.firstinspires.ftc.teamcode.Dragon;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Dragon Example")
public class DragonExample extends OpMode {
    private Dragon dragon;
    
    @Override
    public void init() {
        dragon = new Dragon(hardwareMap, telemetry);
    }
    
    @Override
    public void loop() {
        // Take off
        if (gamepad1.a) {
            dragon.takeOff();
        }
        
        // Land
        if (gamepad1.b) {
            dragon.land();
        }
        
        // Breathe fire
        if (gamepad1.x) {
            dragon.breatheFire();
        }
        
        // Target peasant at coordinates
        if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) {
            double targetX = gamepad1.left_stick_x * 48.0;
            double targetY = gamepad1.left_stick_y * 48.0;
            dragon.targetPeasant(targetX, targetY);
        }
        
        // Update dragon systems
        dragon.update();
    }
    
    @Override
    public void stop() {
        dragon.stopAll();
    }
}
```

### Advanced Usage with Robot Integration

```java
import org.firstinspires.ftc.teamcode.Dragon;
import org.firstinspires.ftc.teamcode.Robot;

public class RobotWithDragon extends OpMode {
    private Robot robot;
    private Dragon dragon;
    
    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        dragon = new Dragon(hardwareMap, telemetry);
    }
    
    @Override
    public void loop() {
        // Control robot normally
        robot.drive(Robot.DriveState.FIELD_CENTRIC, gamepad1, 1.0);
        
        // Add dragon capabilities
        if (gamepad2.dpad_up) {
            dragon.takeOff();
        }
        if (gamepad2.dpad_down) {
            dragon.land();
        }
        if (gamepad2.right_bumper && dragon.isTargetInRange()) {
            dragon.breatheFire();
        }
        
        dragon.update();
    }
}
```

## API Reference

### Constructor

#### `Dragon(HardwareMap hardwareMap, Telemetry telemetry)`
Creates a new Dragon instance.
- **Parameters:**
  - `hardwareMap`: Robot's hardware map for accessing motors and servos
  - `telemetry`: Telemetry for status reporting

### Flight Methods

#### `void takeOff()`
Initiates dragon flight. Dragon will gradually ascend to flight altitude.

#### `void land()`
Initiates landing sequence. Dragon will descend to ground level.

#### `void updateFlight()`
Updates flight mechanics. Must be called in loop for continuous flight.

#### `boolean isFlying()`
Returns current flight status.
- **Returns:** `true` if dragon is flying

#### `double getAltitude()`
Gets current altitude above terrain.
- **Returns:** Current altitude in inches

### Fire-Breathing Methods

#### `boolean breatheFire()`
Activates fire-breathing attack.
- **Returns:** `true` if fire breath activated, `false` if on cooldown

#### `void updateFireBreath()`
Updates fire-breathing animation and damage. Must be called in loop.

#### `boolean isBreathingFire()`
Returns fire-breathing status.
- **Returns:** `true` if currently breathing fire

#### `double getFireBreathCooldown()`
Gets remaining cooldown time.
- **Returns:** Seconds until fire breath is ready

### Targeting Methods

#### `void targetPeasant(double x, double y)`
Targets a peasant at specified coordinates.
- **Parameters:**
  - `x`: X coordinate of target
  - `y`: Y coordinate of target

#### `boolean isTargetInRange()`
Checks if target is within fire breath range.
- **Returns:** `true` if target is in range

### Utility Methods

#### `void update()`
Performs complete update cycle for all dragon systems. Call this in your OpMode loop.

#### `void stopAll()`
Emergency stop - stops all motors and servos immediately.

## Configuration Constants

You can customize dragon behavior by modifying these constants in `Dragon.java`:

| Constant | Default | Description |
|----------|---------|-------------|
| `MAX_FLIGHT_ALTITUDE` | 100.0 | Maximum flight altitude (inches) |
| `FLIGHT_SPEED` | 0.8 | Wing flapping speed (0-1) |
| `HOVER_POWER` | 0.3 | Power to maintain altitude |
| `FIRE_BREATH_RANGE` | 48.0 | Fire breath range (inches) |
| `FIRE_BREATH_DURATION` | 2.0 | Fire breath duration (seconds) |
| `FIRE_BREATH_DAMAGE` | 50.0 | Damage per second |
| `FIRE_BREATH_COOLDOWN` | 3.0 | Cooldown between breaths (seconds) |

## Testing

### DragonOpMode
Use the included `DragonOpMode` for testing:

1. Deploy code to Robot Controller
2. Select "Dragon Control" from TeleOp OpModes
3. Use gamepad controls:
   - **A Button**: Toggle flight mode
   - **B Button**: Breathe fire
   - **Left Stick**: Target peasants

### Expected Behavior

**Flight Test:**
1. Press A to take off
2. Dragon should gradually ascend
3. Telemetry shows increasing altitude
4. Wing motors oscillate for flight animation
5. Press A again to land

**Fire-Breathing Test:**
1. Position target using left stick
2. Press B to breathe fire
3. Fire animation runs for 2 seconds
4. 3-second cooldown before next breath
5. Telemetry shows damage dealt

**Targeting Test:**
1. Move left stick to different positions
2. Head servo adjusts to aim at target
3. Telemetry shows "In Fire Range" status
4. Fire breath only affects targets in range

## SolversLib Integration

This implementation uses SolversLib classes for hardware control:

- **MotorEx**: Advanced motor control with encoder support
- **ServoEx**: Enhanced servo control with position mapping
- **Motor.GoBILDA**: Motor specifications for accurate control
- **Motor.RunMode**: Run modes (RawPower, VelocityControl)
- **Motor.ZeroPowerBehavior**: Brake mode for precise control

All hardware interactions follow SolversLib best practices for maintainability and compatibility with the competition bot framework.

## Troubleshooting

### Dragon Won't Take Off
- Verify all motors are configured in hardware map
- Check motor directions (left wing should be normal, right wing inverted)
- Ensure motors are not in an error state
- Check power supply to motors

### Fire Breath Not Working
- Verify servos are configured correctly
- Check cooldown timer (wait 3 seconds between breaths)
- Ensure target is within range (48 inches)
- Verify fire breath servo is properly connected

### Targeting Issues
- Check head servo connection
- Verify coordinate calculations
- Ensure gamepad inputs are being read
- Check servo position limits (0.0 - 1.0)

### Motor Overheating
- Reduce flight speed constant
- Add delays between flight operations
- Check for mechanical resistance
- Ensure adequate power supply

## Future Enhancements

Possible improvements for future versions:

1. **Advanced Flight Patterns**: Implement circular flight, figure-8 patterns
2. **Multiple Fire Types**: Ice breath, lightning breath variants
3. **Autonomous Peasant Detection**: Camera-based target acquisition
4. **Formation Flying**: Multiple dragons in coordinated flight
5. **Damage Zones**: Area-of-effect fire breath
6. **Energy Management**: Stamina/mana system for abilities

## Contributing

When modifying the Dragon class:
1. Follow existing SolversLib patterns
2. Document all public methods
3. Update this documentation
4. Test thoroughly before committing
5. Consider backward compatibility

## License

This code is part of the Techalongs competition bot and follows the FTC SDK license.

## Support

For questions or issues:
- Check FTC forums: https://ftc-community.firstinspires.org/
- SolversLib docs: https://docs.seattlesolvers.com/
- Team documentation: See TeamCode README.md

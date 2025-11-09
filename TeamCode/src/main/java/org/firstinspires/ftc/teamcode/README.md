# Techalongs Team Code

## Running

This projects has `TeamCode` and `SlothLoad` run configurations saved as project files.

- TeamCode: Build and deploy the code to the robot.
- SlothLoad: After deploying the code to the robot, run this to update just the changed code.  
             You do not need to restart the robot but you do need to stop and start the OpMode.

## Dragon Flight and Fire-Breathing System

The Dragon class provides advanced flight mechanics and fire-breathing capabilities for the competition bot.

### Features
- **Flight Mechanics**: Dragon can fly up to 100 inches above terrain with altitude control
- **Fire-Breathing**: Configurable fire breath with range, damage, and cooldown system
- **Peasant Targeting**: Coordinate-based targeting with automatic range detection
- **SolversLib Integration**: Full integration with SolversLib for maintainability

### Quick Start
```java
Dragon dragon = new Dragon(hardwareMap, telemetry);
dragon.takeOff();              // Start flying
dragon.targetPeasant(24, 12);  // Target peasant at coordinates
dragon.breatheFire();          // Breathe fire!
dragon.update();               // Update in loop
```

### Documentation
- [Dragon Documentation](docs/Dragon.md) - Complete API reference and usage guide
- Example OpModes:
  - `DragonOpMode.java` - Manual control example
  - `DragonAttackDemo.java` - Autonomous attack sequence
  - `DragonTest.java` - System test suite

## SolversLib / FTCLib

- [SolversLib Docs](https://docs.seattlesolvers.com/solverslib-docs-beta-0.3.3)
- [SolversLib GitHub](https://github.com/FTC-23511/SolversLib)


## Pedro Pathing

- [Pedro Pathing Docs](https://pedropathing.com/docs/pathing)
- [Pedro Pathing GitHub](https://github.com/Pedro-Pathing/PedroPathing)
- [SolversLib Pedro Commands](https://docs.seattlesolvers.com/pedro-pathing/pedro-commands)

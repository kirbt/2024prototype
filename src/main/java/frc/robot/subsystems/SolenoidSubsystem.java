package frc.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SolenoidSubsystem extends SubsystemBase {

    // There are solenoids and double solenoids, here is the difference:

    // a single acting solenoid has one singular coil. When energized, it will move the valve to a
    // position, and when not energized will return.

    // a double acting solenoid has two seperate coils. When one is energized, it will move to an A position, when 
    // the other is energized instead, it will move to the B position. When not energized, it will not move.

    // Here's an example of usage:
    // Solenoids are often used for pneumatic cylinders, which are kinda like rods that extend and retract i guess
    // If you want to make sure the solenoid stays in whatever position it is after the robot is disabled, then you opt
    // for a double solenoid, if you want to return to a default position when disabled, you opt for the single acting
    // solenoid.
    
    // It is important to understand 

    // This subsystem code will be a pneumatic cylinder using a double solenoid.

    private final int FORWARD_CHANNEL = 0;
    private final int REVERSE_CHANNEL = 1;

    private final Map<DoubleSolenoid.Value, String> STATES = Map.of(DoubleSolenoid.Value.kForward, "EXTENDED", DoubleSolenoid.Value.kReverse, "RETRACTED", DoubleSolenoid.Value.kOff, "OFF");

    // hardware
    private final DoubleSolenoid solenoid;

    // shuffleboard logging
    private ShuffleboardTab shuffleboardTab;


    public SolenoidSubsystem() {
        solenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, FORWARD_CHANNEL, REVERSE_CHANNEL);

        initShuffleboard();
    }

    public void extend() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void retract() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    private boolean isExtended() {
        return (solenoid.get().equals(DoubleSolenoid.Value.kForward));
    }

    private void initShuffleboard() {
        shuffleboardTab = Shuffleboard.getTab("SolenoidSubsystem");

        shuffleboardTab.addString("State", () -> STATES.get(solenoid.get()));

    }

}

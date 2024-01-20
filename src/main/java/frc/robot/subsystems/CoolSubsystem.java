package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class CoolSubsystem extends SubsystemBase {


    // DEVICE IDS
    private static final int TOP_FEED_ID = 0;
    private static final int BOTTOM_FEED_ID = 1;
    private static final int TOP_SHOOTER_ID = 2;
    private static final int BOTTOM_SHOOTER_ID = 3;

    // CONSTANTS

    // -1 - 1 value
    private static final double FEEDING_SPEED = 1;
    private static final double SHOOTING_SPEED = 1;

    // MOTORS

    public CANSparkFlex topFeedMotor;
    public CANSparkFlex bottomFeedMotor;
    public CANSparkFlex topShooterMotor;
    public CANSparkFlex bottomShooterMotor;

    // CONSTRUCTOR

    public CoolSubsystem () {

        topFeedMotor = new CANSparkFlex(TOP_FEED_ID, MotorType.kBrushless);
        bottomFeedMotor = new CANSparkFlex(BOTTOM_FEED_ID, MotorType.kBrushless);
        topShooterMotor = new CANSparkFlex(TOP_SHOOTER_ID, MotorType.kBrushless);
        bottomShooterMotor = new CANSparkFlex(BOTTOM_SHOOTER_ID, MotorType.kBrushless);

        configMotors();
    }

    public void configMotors() {

        bottomFeedMotor.follow(topFeedMotor);
        bottomShooterMotor.follow(topShooterMotor);

        bottomFeedMotor.setInverted(true);
        bottomShooterMotor.setInverted(true);

        topFeedMotor.setIdleMode(IdleMode.kCoast);
        bottomFeedMotor.setIdleMode(IdleMode.kCoast);
        topShooterMotor.setIdleMode(IdleMode.kCoast);
        bottomShooterMotor.setIdleMode(IdleMode.kCoast);
    }

    public void feedForward() {
        topFeedMotor.set(FEEDING_SPEED);
    }

    public void shootForward() {
        topShooterMotor.set(SHOOTING_SPEED);
    }

    public void stopFeedMotors() {
        topFeedMotor.set(0);
    }

    public void stopShooterMotors() {
        topShooterMotor.set(0);
    }
    
}

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;

import java.util.Map;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class CoolSubsystem extends SubsystemBase {


    // DEVICE IDS
    //30 - 31 - 32 - 33
    private static final int TOP_FEED_ID = 31;
    private static final int BOTTOM_FEED_ID = 30;
    private static final int TOP_SHOOTER_ID = 33;
    private static final int BOTTOM_SHOOTER_ID = 32;

    // CONSTANTS

    // -1 - 1 value
    private static final double FEEDING_SPEED = 0.20;
    private static final double SHOOTING_SPEED = 0.5;

    //pid

    private static final double FEEDER_P = 0;
    private static final double FEEDER_I = 0;
    private static final double FEEDER_D = 0;

    private static final double SHOOTER_P = 0;
    private static final double SHOOTER_I = 0;
    private static final double SHOOTER_D = 0;

    // MOTORS

    public CANSparkFlex topFeedMotor;
    public CANSparkFlex bottomFeedMotor;
    public CANSparkFlex topShooterMotor;
    public CANSparkFlex bottomShooterMotor;

    // changable speeds

    public double topFeedingSpeed;
    public double bottomFeedingSpeed;
    public double topShootingSpeed;
    public double bottomShootingSpeed;

    //PID

    private final SparkPIDController topFeedMotorPidController;
    private final SparkPIDController topShooterMotorPidController;
    private final SparkPIDController bottomFeedMotorPidController;
    private final SparkPIDController bottomShooterMotorPidController;


    // logging

    NetworkTable networkTable;

    GenericEntry feedingSpeedEntry =
			Shuffleboard.getTab("Cool Subsystem")
					.add("Feeding Speed", FEEDING_SPEED)
					.withSize(2, 1)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", -1, "Max", 1))
					.getEntry();

    GenericEntry shootingSpeedEntry =
			Shuffleboard.getTab("Cool Subsystem")
					.add("Shooting Speed", SHOOTING_SPEED)
					.withSize(2, 1)
					.withWidget(BuiltInWidgets.kNumberSlider)
					.withProperties(Map.of("Min", -1, "Max", 1))
					.getEntry();
    // CONSTRUCTOR

    public CoolSubsystem () {

        topFeedMotor = new CANSparkFlex(TOP_FEED_ID, MotorType.kBrushless);
        bottomFeedMotor = new CANSparkFlex(BOTTOM_FEED_ID, MotorType.kBrushless);
        topShooterMotor = new CANSparkFlex(TOP_SHOOTER_ID, MotorType.kBrushless);
        bottomShooterMotor = new CANSparkFlex(BOTTOM_SHOOTER_ID, MotorType.kBrushless);
        topFeedMotorPidController = topFeedMotor.getPIDController();
        bottomFeedMotorPidController = bottomFeedMotor.getPIDController();
        topShooterMotorPidController = topShooterMotor.getPIDController();
        bottomShooterMotorPidController = topShooterMotor.getPIDController();

        // logging 
        
        networkTable = NetworkTableInstance.getDefault().getTable("coolsubsystem");
	    ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("Cool Subsystem");

        shuffleboardTab.addDouble("Top Feeding Motor", this::getTopFeedingSpeed).withPosition(5, 0).withSize(1, 1);
        shuffleboardTab.addDouble("Bottom Feeding Motor", this::getBottomFeedingSpeed).withPosition(6, 0).withSize(1, 1);
        shuffleboardTab.addDouble("Top Shooting Motor", this::getTopShootingSpeed).withPosition(5, 1).withSize(1, 1);
        shuffleboardTab.addDouble("Bottom Shooting Motor", this::getBottomShootingSpeed).withPosition(6, 1).withSize(1, 1);

        configMotors();
    }

    public void configMotors() {

        // bottomFeedMotor.follow(topFeedMotor);
        // bottomShooterMotor.follow(topShooterMotor);

        // bottomFeedMotor.setInverted(false);
        // bottomShooterMotor.setInverted(false);

        topFeedMotor.setIdleMode(IdleMode.kCoast);
        bottomFeedMotor.setIdleMode(IdleMode.kCoast);
        topShooterMotor.setIdleMode(IdleMode.kCoast);
        bottomShooterMotor.setIdleMode(IdleMode.kCoast);

        bottomFeedMotor.burnFlash();
        topFeedMotor.burnFlash();
        bottomShooterMotor.burnFlash();
        topShooterMotor.burnFlash();


    }

    public void setPID() {
        topFeedMotorPidController.setP(FEEDER_P);
        topFeedMotorPidController.setP(FEEDER_I);
        topFeedMotorPidController.setP(FEEDER_D);
        //top and bottom feed
        bottomFeedMotorPidController.setP(SHOOTER_P);
        bottomFeedMotorPidController.setP(SHOOTER_I);
        bottomFeedMotorPidController.setP(SHOOTER_D);

        //shooter
        topShooterMotorPidController.setP(FEEDER_P);
        topShooterMotorPidController.setP(FEEDER_I);
        topShooterMotorPidController.setP(FEEDER_D);

        bottomShooterMotorPidController.setP(SHOOTER_P);
        bottomShooterMotorPidController.setP(SHOOTER_I);
        bottomShooterMotorPidController.setP(SHOOTER_D);
    
    }

    public void feedForward() {
        topFeedMotor.set(FEEDING_SPEED);
        bottomFeedMotor.set(-FEEDING_SPEED);
    }

    public void shootForward() {
        System.out.println("gulp");
        topShooterMotor.set(SHOOTING_SPEED);
        bottomShooterMotor.set(-SHOOTING_SPEED);
    }

    public void stopFeedMotors() {
        topFeedMotor.set(0);
        bottomShooterMotor.set(0);
    }

    public void stopShooterMotors() {
        topShooterMotor.set(0);
        bottomShooterMotor.set(0);
    }

    // logging

    public double getTopShootingSpeed() {
        return topShootingSpeed;
    }

    public double getBottomShootingSpeed() {
        return bottomShootingSpeed;
    }

    public double getTopFeedingSpeed() {
        return topFeedingSpeed;

    }

    public double getBottomFeedingSpeed() {
        return bottomFeedingSpeed;
    }

    // PID methods

    public void setShooterVelocityGoal() {
        topShooterMotorPidController.setReference(SHOOTING_SPEED, ControlType.kVelocity);
        bottomShooterMotorPidController.setReference(SHOOTING_SPEED, ControlType.kVelocity);
    }

    public void setFeederVelocityGoal() {
        topFeedMotorPidController.setReference(FEEDING_SPEED, ControlType.kVelocity);
        bottomFeedMotorPidController.setReference(FEEDING_SPEED, ControlType.kVelocity);
        System.out.println(topFeedMotorPidController.getOutputMax(0));
    }


    @Override
    public void periodic() {
        topShootingSpeed = topFeedMotor.get();
        bottomShootingSpeed = bottomFeedMotor.get();
        topShootingSpeed = topShooterMotor.get();
        bottomShootingSpeed = bottomShooterMotor.get();
    }
    
}

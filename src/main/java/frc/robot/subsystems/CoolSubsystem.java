package frc.robot.subsystems;

import edu.wpi.first.networktables.DoubleEntry;
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

    // defaults 0.20 and 0.5
    private static final double FEEDING_SPEED = 0.15;
    private static final double SHOOTING_SPEED = 0.6;

    //pid

    private static final double FEEDER_P = 0.1;
    private static final double FEEDER_I = 0;
    private static final double FEEDER_D = 0;

    private static final double SHOOTER_P = 0.1;
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

    private SparkPIDController topFeedMotorPidController;
    private SparkPIDController topShooterMotorPidController;
    private SparkPIDController bottomFeedMotorPidController;
    private SparkPIDController bottomShooterMotorPidController;


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
                    
    GenericEntry shooterPEntry = Shuffleboard.getTab("Cool Subsystem").add("shooterP", SHOOTER_P).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();
    GenericEntry shooterIEntry = Shuffleboard.getTab("Cool Subsystem").add("shooterI", SHOOTER_I).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();
    GenericEntry shooterDEntry = Shuffleboard.getTab("Cool Subsystem").add("shooterD", SHOOTER_D).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();

    GenericEntry feederPEntry = Shuffleboard.getTab("Cool Subsystem").add("feederP", FEEDER_P).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();
    GenericEntry feederIEntry = Shuffleboard.getTab("Cool Subsystem").add("feederI", FEEDER_I).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();
    GenericEntry feederDEntry = Shuffleboard.getTab("Cool Subsystem").add("feederD", FEEDER_D).withSize(2, 1).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("Min", -1, "Max", 1)).getEntry();
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

        shuffleboardTab.addDouble("Top Feeding Motor", topFeedMotor::get).withPosition(5, 0).withSize(1, 1);
        shuffleboardTab.addDouble("Bottom Feeding Motor", bottomFeedMotor::get).withPosition(6, 0).withSize(1, 1);
        shuffleboardTab.addDouble("Top Shooting Motor", topShooterMotor::get).withPosition(5, 1).withSize(1, 1);
        shuffleboardTab.addDouble("Bottom Shooting Motor", bottomShooterMotor::get).withPosition(6, 1).withSize(1, 1);

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
        topFeedMotorPidController.setP(feederPEntry.getDouble(FEEDER_P));
        topFeedMotorPidController.setP(feederIEntry.getDouble(FEEDER_I));
        topFeedMotorPidController.setP(feederDEntry.getDouble(FEEDER_D));
        //top and bottom feed
        bottomFeedMotorPidController.setP(feederPEntry.getDouble(FEEDER_P));
        bottomFeedMotorPidController.setP(feederIEntry.getDouble(FEEDER_I));
        bottomFeedMotorPidController.setP(feederDEntry.getDouble(FEEDER_D));

        //shooter
        topShooterMotorPidController.setP(shooterPEntry.getDouble(SHOOTER_P));
        topShooterMotorPidController.setP(shooterIEntry.getDouble(SHOOTER_D));
        topShooterMotorPidController.setP(shooterDEntry.getDouble(SHOOTER_D));

        bottomShooterMotorPidController.setP(shooterPEntry.getDouble(SHOOTER_P));
        bottomShooterMotorPidController.setP(shooterIEntry.getDouble(SHOOTER_I));
        bottomShooterMotorPidController.setP(shooterDEntry.getDouble(SHOOTER_D));
    
    }

    public void feedForward() {
        topFeedMotor.set(feedingSpeedEntry.getDouble(FEEDING_SPEED));
        bottomFeedMotor.set(-feedingSpeedEntry.getDouble(FEEDING_SPEED));
        //setFeederVelocityGoal(feedingSpeedEntry.getDouble(FEEDING_SPEED));
    }

    public void shootForward() {
        System.out.println("gulp");
        topShooterMotor.set(shootingSpeedEntry.getDouble(SHOOTING_SPEED));
        bottomShooterMotor.set(-shootingSpeedEntry.getDouble(SHOOTING_SPEED));
        //setShooterVelocityGoal(shootingSpeedEntry.getDouble(SHOOTING_SPEED));
    }

    public void stopFeedMotors() {
        topFeedMotor.set(0);
        bottomFeedMotor.set(0);
        //setFeederVelocityGoal(0);
    }

    public void stopShooterMotors() {
        topShooterMotor.set(0);
        bottomShooterMotor.set(0);
        //setShooterVelocityGoal(0);
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

    public void setShooterVelocityGoal(double speed) {
        topShooterMotorPidController.setReference(speed, ControlType.kVelocity);
        bottomShooterMotorPidController.setReference(speed, ControlType.kVelocity);
        System.out.println("shooter velocity was set (wow 2x)");
    }

    public void setFeederVelocityGoal(double speed) {
        topFeedMotorPidController.setReference(speed, ControlType.kVelocity);
        bottomFeedMotorPidController.setReference(speed, ControlType.kVelocity);
        System.out.println(topFeedMotorPidController.getOutputMax(0));
        System.out.println("feeder velocity was set (wow)");
    }


    @Override
    public void periodic() {
        topShootingSpeed = topFeedMotor.get();
        bottomShootingSpeed = bottomFeedMotor.get();
        topShootingSpeed = topShooterMotor.get();
        bottomShootingSpeed = bottomShooterMotor.get();

        setPID();
    }
    
}

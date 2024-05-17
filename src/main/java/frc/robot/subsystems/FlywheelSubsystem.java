package frc.team2412.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FlywheelSubsystem extends SubsystemBase {

	// CONSTANTS

	// hardware port ids (usually stored in a different file)
	private final int TOP_MOTOR_ID = 0;
	private final int BOTTOM_MOTOR_ID = 1;

	private final int FLYWHEEL_CURRENT_LIMIT = 60;

	private final double FLYWHEEL_P = 0.1;
	private final double FLYWHEEL_I = 0;
	private final double FLYWHEEL_D = 0.25;

	private final double RPM_TOLERANCE = 200;

	// OBJECTS/INSTANCES

	// Shuffleboard (To keep track of/log necesscary values)
	private ShuffleboardTab tab;

	// motors
	private final CANSparkFlex topMotor;
	private final CANSparkFlex bottomMotor;

	private final RelativeEncoder topMotorEncoder;
	private final RelativeEncoder bottomMotorEncoder;

	// pid controllers
	private final SparkPIDController topPIDController;
	private final SparkPIDController bottomPIDController;

	// state variables (currently for logging purposes)
	private double topSetVelocity; // in RPM
	private double bottomSetVelocity; // in RPM

	public FlywheelSubsystem() {
		// fetches the motor in question using motor id
		topMotor = new CANSparkFlex(TOP_MOTOR_ID, MotorType.kBrushless);
		bottomMotor = new CANSparkFlex(BOTTOM_MOTOR_ID, MotorType.kBrushless);

		// fetches the encoder in question using the motor
		topMotorEncoder = topMotor.getEncoder();
		bottomMotorEncoder = bottomMotor.getEncoder();

		// fetches the built in pid controller using the motor
		topPIDController = topMotor.getPIDController();
		bottomPIDController = bottomMotor.getPIDController();

		topSetVelocity = 0;
		bottomSetVelocity = 0;

		// configure the motors upon subsystem initialization
		configMotors();
		configPID();

		// logging values
		initShuffleboard();
	}

	// METHODS

	// config methods
	private void configPID() {
		topPIDController.setP(FLYWHEEL_P);
		topPIDController.setI(FLYWHEEL_I);
		topPIDController.setD(FLYWHEEL_D);

		bottomPIDController.setP(FLYWHEEL_P);
		bottomPIDController.setI(FLYWHEEL_I);
		bottomPIDController.setD(FLYWHEEL_D);
	}

	private void configMotors() {
		// reset to factory default at the start of configuration
		topMotor.restoreFactoryDefaults();
		bottomMotor.restoreFactoryDefaults();

		// idle modes
		topMotor.setIdleMode(IdleMode.kCoast);
		bottomMotor.setIdleMode(IdleMode.kCoast);

		// motor inversion
		bottomMotor.setInverted(true);

		// current limits
		topMotor.setSmartCurrentLimit(FLYWHEEL_CURRENT_LIMIT);
		bottomMotor.setSmartCurrentLimit(FLYWHEEL_CURRENT_LIMIT);

		// make sure to burn flash so configurations are set
		topMotor.burnFlash();
		bottomMotor.burnFlash();
	}

	// motor control
	private void setTopMotorVelocity(double velocity) {
		topPIDController.setReference(velocity, ControlType.kVelocity);
		topSetVelocity = velocity;
	}

	private void setBottomMotorVelocity(double velocity) {
		bottomPIDController.setReference(velocity, ControlType.kVelocity);
		bottomSetVelocity = velocity;
	}

	private void stopTopMotor() {
		topMotor.stopMotor();
		topSetVelocity = 0;
	}

	private void stopBottomMotor() {
		bottomMotor.stopMotor();
		bottomSetVelocity = 0;
	}

	private boolean flywheelsAtSpeed() {
		return MathUtil.isNear(topSetVelocity, topMotorEncoder.getVelocity(), RPM_TOLERANCE)
				&& MathUtil.isNear(bottomSetVelocity, bottomMotorEncoder.getVelocity(), RPM_TOLERANCE);
	}

	// logging
	private void initShuffleboard() {
		tab = Shuffleboard.getTab("FlywheelSubsystem");

		tab.addDouble("Top Motor Velocity", () -> topMotorEncoder.getVelocity());
		tab.addDouble("Bottom Motor Velocity", () -> bottomMotorEncoder.getVelocity());

		tab.addDouble("Top Motor Set Velocity", () -> topMotor.get());
		tab.addDouble("Bottom Motor Set Velocity", () -> bottomMotor.get());

		tab.addBoolean("Flywheels Ready", () -> flywheelsAtSpeed());
	}

}

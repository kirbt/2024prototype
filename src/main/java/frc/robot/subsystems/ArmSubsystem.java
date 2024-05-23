package frc.robot.subsystems;

import java.util.Map;
import java.util.Optional;

import javax.print.attribute.standard.PresentationDirection;
import javax.swing.text.html.Option;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase{
    
    // CONSTANTS

    private final int LEFT_MOTOR_ID = 2;
    private final int RIGHT_MOTOR_ID = 3;

    private final double MAX_ROTATION = 0.0;
    private final double MIN_ROTATION = 1.0;
    private final int PIVOT_CURRENT_LIMIT = 100;

    private final double POSITION_TOLERANCE = 0.05;

    private final double PIVOT_P = 5.2;
    private final double PIVOT_I = 0.0;
    private final double PIVOT_D = 0.02;


    // OBJECTS/INSTANCES

    //hardware

    private final CANSparkMax leftMotor;
    private final CANSparkMax rightMotor;
    private final SparkAbsoluteEncoder pivotEncoder;

    private final SparkPIDController leftPidController;
    private final SparkPIDController rightPidController;

    //shuffleboard
    private ShuffleboardTab shuffleboardTab;
    private GenericEntry pivotGoalEntry;

    private double pivotGoal;
    private Optional<Double> prevSetPosition;
    private NetworkTableInstance ntInstance;

    public ArmSubsystem() {
        leftMotor = new CANSparkMax(LEFT_MOTOR_ID, MotorType.kBrushless);
        rightMotor = new CANSparkMax(RIGHT_MOTOR_ID, MotorType.kBrushless);

        pivotEncoder = leftMotor.getAbsoluteEncoder();

        leftPidController = leftMotor.getPIDController();
        rightPidController = rightMotor.getPIDController();
        
    }


    private void configMotors() {
        leftMotor.restoreFactoryDefaults();
        rightMotor.restoreFactoryDefaults();

        rightMotor.follow(leftMotor);
        rightMotor.setInverted(true);

        configPID();

        leftMotor.burnFlash();
        rightMotor.burnFlash();
    }

    private void configPID() {
        leftPidController.setP(PIVOT_P);
        leftPidController.setI(PIVOT_I);
        leftPidController.setD(PIVOT_D);

        rightPidController.setP(PIVOT_P);
        rightPidController.setI(PIVOT_I);
        rightPidController.setD(PIVOT_D);
    }


    // motor control

    public void setPivotGoal(double position) {
        // clamp goal positions that go past limits
        pivotGoal = MathUtil.clamp(position, MIN_ROTATION, MAX_ROTATION);

        pivotPidController.setReference(position, ControlType.kPosition);
    }

    public double getPosition() {
        return pivotEncoder.getPosition();
    }

    public boolean isPivotAtPosition() {
        return MathUtil.isNear(pivotGoal, getPosition(), POSITION_TOLERANCE);
    }

    private void initShuffleboard() {
        shuffleboardTab = Shuffleboard.getTab("ArmSubsystem");

        shuffleboardTab.addDouble("Position Goal", () -> pivotGoal);
        shuffleboardTab.addDouble("Current Position", this::getPosition);
        shuffleboardTab.addBoolean("Pivot At Position", this::isPivotAtPosition);
        
        pivotGoalEntry = shuffleboardTab.add("Manual Set Position", 0).withProperties(Map.of("Min", MIN_ROTATION, "Max", MAX_ROTATION)).getEntry();
        prevSetPosition = Optional.of(pivotGoalEntry.getDouble(0));
    }

    @Override
    public void periodic() {

        // on changing the manual set position entry, set the arm position goal.
        double setPosition = pivotGoalEntry.getDouble(0);

        if (prevSetPosition.get() != setPosition) {
            setPivotGoal(setPosition);
            prevSetPosition = Optional.of(setPosition);
        }
    }
}

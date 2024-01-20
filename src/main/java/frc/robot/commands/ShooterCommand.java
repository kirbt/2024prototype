package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoolSubsystem;

public class ShooterCommand extends Command {
    
    CoolSubsystem coolSubsystem;

    public ShooterCommand(CoolSubsystem coolSubsystem) {
        this.coolSubsystem = coolSubsystem;
    }

    @Override
    public void initialize() {
        coolSubsystem.setShooterVelocityGoal();
    }

    @Override
    public void execute() {
        // coolSubsystem.shootForward();
        coolSubsystem.setShooterVelocityGoal();
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}

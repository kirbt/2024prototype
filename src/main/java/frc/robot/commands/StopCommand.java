package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoolSubsystem;

public class StopCommand extends Command {
    

    CoolSubsystem coolSubsystem;

    public StopCommand(CoolSubsystem coolSubsystem) {
        this.coolSubsystem = coolSubsystem;

    }

    @Override
    public void initialize() {
        coolSubsystem.stopFeedMotors();
        coolSubsystem.stopShooterMotors();
    }

    
    @Override
    public boolean isFinished() {
        return true;
    }
}

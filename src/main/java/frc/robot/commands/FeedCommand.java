package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoolSubsystem;

public class FeedCommand extends Command {

    CoolSubsystem coolSubsystem;

    public FeedCommand (CoolSubsystem coolSubsystem) {
        this.coolSubsystem = coolSubsystem;
    }

    @Override
    public void initialize() {
        coolSubsystem.feedForward();
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }

}

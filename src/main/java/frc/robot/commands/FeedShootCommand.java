package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.CoolSubsystem;

public class FeedShootCommand extends ParallelCommandGroup {

    CoolSubsystem coolSubsystem;

    public FeedShootCommand(CoolSubsystem coolSubsystem) {
        this.coolSubsystem = coolSubsystem;
        addCommands(new FeedCommand(coolSubsystem), new ShooterCommand(coolSubsystem));
    }

    
}

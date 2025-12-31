package org.firstinspires.ftc.teamcode.subsystems;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import org.firstinspires.ftc.teamcode.Constants;

public class Intake implements Subsystem {

public static final Intake INSTANCE = new Intake();

private MotorEx intakeMotor;

private final ControlSystem intakeCalculator = ControlSystem.builder()
        .velPid(
                Constants.IntakeConstants.intake_kP,
                Constants.IntakeConstants.intake_kI,
                Constants.IntakeConstants.intake_kD
        )
        .basicFF(Constants.IntakeConstants.intake_kF)
        .build();

private Intake() {}

@Override
public void initialize() {
    intakeMotor = new MotorEx(Constants.IntakeConstants.intakeName).brakeMode().zeroed();
}

@Override
public void periodic() {
    intakeMotor.setPower(intakeCalculator.calculate(intakeMotor.getState()));
}

public Command spinIntake() {
    return new RunToVelocity(intakeCalculator, Constants.IntakeConstants.intakeSpeed).addRequirements(this);
}

public Command stopIntake() {
    return new RunToVelocity(intakeCalculator, 0.0).addRequirements(this);
}
}



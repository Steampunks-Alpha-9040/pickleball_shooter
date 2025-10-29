package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.FeedbackType;
import dev.nextftc.control.feedback.PIDElement;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.control.filters.FilterElement;
import dev.nextftc.control.interpolators.ConstantInterpolator;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.delegates.AnalogFeedback;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel;

    private CRServoEx hood;

    private AnalogFeedback encoder;

    private final ControlSystem calculator = new ControlSystem(
            new PIDElement(
                    FeedbackType.VELOCITY,
                    Constants.FlywheelConstants.kP,
                    Constants.FlywheelConstants.kI,
                    Constants.FlywheelConstants.kD
                    ),
            new BasicFeedforward(
                    Constants.FlywheelConstants.kF
            ),
            new FilterElement(),
            new ConstantInterpolator(new KineticState(0.0))

    );


    private Flywheel(){}

    @Override
    public void initialize(){
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
    }

    public Command spinFlywheelFast(){
        return new LambdaCommand("spinFlywheel")
                .requires(this)
                .setUpdate(() -> setFlywheel(1.0))
                .setInterruptible(true);
    }

    public Command spinFlywheelSlow(){
        return new LambdaCommand("spinFlywheel")
                .requires(this)
                .setUpdate(() -> setFlywheel(0.5))
                .setInterruptible(true);
    }

    public Command stopFlywheel(){
        return new LambdaCommand("spinFlywheel")
                .requires(this)
                .setUpdate(() -> setFlywheel(0.0))
                .setInterruptible(true);
    }

    public void setFlywheel(double power){
        calculator.setGoal(new KineticState(power));
        flywheel.setPower(calculator.calculate());
    }




}

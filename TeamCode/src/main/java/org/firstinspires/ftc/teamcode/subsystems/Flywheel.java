package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.FeedbackType;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDElement;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.control.filters.FilterElement;
import dev.nextftc.control.interpolators.ConstantInterpolator;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel;

    private ControlSystem calculator = new ControlSystem(
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
            new ConstantInterpolator()

    )

    private Flywheel(){}

    @Override
    public void initialize(){
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed();
    }

    public Command spinFlywheel(){
        return new LambdaCommand("spinFlywheel")
                .setStart(() -> flywheel.setPower(1.0))
                .setIsDone(() -> Gamepads.gamepad1().a().get());
    }

    public void powerFlywheel(double power){
        flywheel.setPower(power);
    }




}

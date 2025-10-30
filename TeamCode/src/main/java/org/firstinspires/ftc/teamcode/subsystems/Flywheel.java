package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.AnalogSensor;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel;

    private CRServoEx hood;

    private AnalogSensor encoder;

    private double encoderRotations;

    private double curEncoder = 0.0;
    private double prevEncoder = 0.0;

    private final ControlSystem flywheelCalculator = ControlSystem.builder()
            .velPid(
                    Constants.FlywheelConstants.flywheel_kP,
                    Constants.FlywheelConstants.flywheel_kI,
                    Constants.FlywheelConstants.flywheel_kD
            )
            .basicFF(
                    Constants.FlywheelConstants.flywheel_kF
            )
            .build();

    private final ControlSystem hoodCalculator = ControlSystem.builder()
            .posPid(
                    Constants.FlywheelConstants.hood_kP,
                    Constants.FlywheelConstants.hood_kI,
                    Constants.FlywheelConstants.hood_kD
            )
            .basicFF(
                    Constants.FlywheelConstants.hood_kF
            )
            .build();


    private Flywheel(){}

    @Override
    public void initialize(){
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
        encoder = ActiveOpMode.hardwareMap().get(AnalogSensor.class, "hoodEnc");
    }

    @Override
    public void periodic(){
        updateHoodPos();
        flywheel.setPower(flywheelCalculator.calculate(flywheel.getState()));
        hood.setPower(hoodCalculator.calculate(getHoodPhysicalState()));
    }

    //jank asf code for axon abs encoders... probably doesn't work.
    private KineticState getHoodPhysicalState(){
        return new KineticState(getEncoderRotations());
    }
    private void updateHoodPos(){
        curEncoder = (encoder.readRawVoltage() * 3.2)/ 360;
        if (prevEncoder - curEncoder > 0.97){
            encoderRotations++;
        } else if (prevEncoder - curEncoder < 355){
            encoderRotations--;
        }
        prevEncoder = curEncoder;
    }

    private double getEncoderRotations(){
        return curEncoder + encoderRotations;
    }



    public Command spinFlywheelFast(){
        return new RunToVelocity(flywheelCalculator, 1.0).addRequirements(this).named("fastFlywheel");
    }
    public Command spinFlywheelSlow(){
        return new RunToVelocity(flywheelCalculator, 0.5).addRequirements(this).named("slowFlywheel");
    }
    public Command stopFlywheel(){
        return new RunToVelocity(flywheelCalculator, 0.0).addRequirements(this).named("stopFlywheel");
    }
    public Command farShooting(){
        return new RunToPosition(hoodCalculator, 1.5)
    }







}

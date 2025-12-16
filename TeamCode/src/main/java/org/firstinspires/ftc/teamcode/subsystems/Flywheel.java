package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import java.util.function.DoubleSupplier;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
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

    private AnalogInput encoder;

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
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed().reversed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
        encoder = ActiveOpMode.hardwareMap().get(AnalogInput.class, "hoodEnc");
    }

    @Override
    public void periodic(){
        updateHoodPos();
        flywheel.setPower(flywheelCalculator.calculate(flywheel.getState()));
//        hood.setPower(hoodCalculator.calculate(getHoodPhysicalState()));
    }

    public void log(PanelsTelemetry telemetry){
        telemetry.getTelemetry().addData("flyVeloRPS:", flywheel.getVelocity() * Util.GoBILDA.BARE.getCPR());
    }

    //jank asf code for axon abs encoders... probably doesn't work.
    private KineticState getHoodPhysicalState(){
        return new KineticState(getEncoderRotations());
    }
    private void updateHoodPos(){
        curEncoder = (encoder.getVoltage() / 3.3);
        if (prevEncoder - curEncoder > 0.8){
            encoderRotations++;
        } else if (prevEncoder - curEncoder < -0.8){
            encoderRotations--;
        }
        prevEncoder = curEncoder;
    }
    private double getEncoderRotations(){
        return curEncoder + encoderRotations;
    }


    private double flywheelTarget = 1.0;

    public void setFlywheelTarget(double target) {
        flywheelTarget = target;
    }

    public double getFlywheelTarget() {
        return flywheelTarget;
    }

    //Commands
    public Command shootFlywheelFar() {
        return new ParallelGroup(
                new RunToVelocity(flywheelCalculator, getFlywheelTarget())
                        .addRequirements(this)
        ).setInterruptible(true);
    }

    public Command shootFlywheelClose(){
        return new ParallelGroup(
            new RunToVelocity(flywheelCalculator, 0.5).addRequirements(this)
        ).named("closeFlywheel");
    }

    public Command stopFlywheel(){
        return new RunToVelocity(flywheelCalculator, 0.0).addRequirements(this);
    }

    public Command spinHoodUp(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(1))
                .requires(this);
    }

    public Command spinHoodDown(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(-1))
                .requires(this);
    }

    public Command stopHood(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(0.0))
                .requires(this);
    }







}

package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDflywheel;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel;

    private CRServoEx hood;

    private final PIDflywheel flywheelCalculator = new PIDflywheel(
            Constants.FlywheelConstants.flywheel_kP,
            Constants.FlywheelConstants.flywheel_kI,
            Constants.FlywheelConstants.flywheel_kD,
            Constants.FlywheelConstants.flywheel_kF,
            Constants.FlywheelConstants.flywheelVelocityTolerance
    );

    private final PIDposition hoodCalculator = new PIDposition(
            Constants.FlywheelConstants.hood_kP,
            Constants.FlywheelConstants.hood_kI,
            Constants.FlywheelConstants.hood_kD,
            Constants.FlywheelConstants.hood_kF,
            Constants.FlywheelConstants.hoodPositionToleranceRAD
    );


    private Flywheel(){}

    @Override
    public void initialize(){
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed().reversed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
    }

    @Override
    public void periodic(){
        flywheel.setPower(flywheelCalculator.calculate(flywheel.getVelocity()));
        hood.setPower(hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));
    }

    public void log(PanelsTelemetry telemetry){
        telemetry.getTelemetry().addData("flyVeloRPS:", flywheel.getVelocity() * Util.GoBILDA.BARE.getCPR());
    }

    public Command shootFlywheel(){
        return new ParallelGroup(
            new InstantCommand(() -> flywheelCalculator.setSetpoint(6000)),
            new InstantCommand(() -> hoodCalculator.setSetpoint(0.5))
        );
    }

    public Command stopFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(0));
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

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
import dev.nextftc.ftc.ActiveOpMode;
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
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).zeroed().reversed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);


    }

    @Override
    public void periodic(){
        double flywheelCurrentRPM = -flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR() * 60;

        shootFlywheel();
        spinHood();

        double power = flywheelCalculator.calculate(flywheelCurrentRPM);

        flywheel.setPower(power);
        hood.setPower(hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

        ActiveOpMode.telemetry().addData("flywheelPIDval", flywheelCalculator.calculate((flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR()) * 60));
        ActiveOpMode.telemetry().addData("hoodPIDval", hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));
        ActiveOpMode.telemetry().addData("power", power);


        ActiveOpMode.telemetry().addData("flywheelVeloTarget", flywheelCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("flywheelVeloCurrent", (flywheelCurrentRPM));
        ActiveOpMode.telemetry().addData("hoodPosTarget", hoodCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("hoodPosCurrent", Drivebase.INSTANCE.getHoodQuadature());
    }


    public Command shootFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(getFlywheelRPM()));
    }

    public Command stopFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(0));
    }

    public void spinHood(){
        hoodCalculator.setSetpoint(getHoodAngle());
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

    public double getFlywheelRPM(){
        if (Drivebase.INSTANCE.getFollower().getPose().getY() < 40){
            return Constants.FlywheelConstants.flywheelVals[0][2];
        } else {
            return 3000;
        }
    }

    public double getHoodAngle(){
        if (Drivebase.INSTANCE.getFollower().getPose().getY() < 40){
            return Constants.FlywheelConstants.flywheelVals[0][3];
        } else {
            return 0.3;
        }
    }


    public Command stopHood(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(0.0))
                .requires(this);
    }







}

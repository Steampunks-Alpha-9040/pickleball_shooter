package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDflywheel;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.RelativeShooting;
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

    private RelativeShooting relativeShooting = new RelativeShooting();

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
        relativeShooting.update();

        shootFlywheel();
        spinHood();

        double power = flywheelCalculator.calculate(flywheelCurrentRPM);

        flywheel.setPower(power);
        hood.setPower(hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

//        ActiveOpMode.telemetry().addData("flywheelPIDval", flywheelCalculator.calculate((flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR()) * 60));
//        ActiveOpMode.telemetry().addData("hoodPIDval", hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));
        ActiveOpMode.telemetry().addData("power", power);
        ActiveOpMode.telemetry().addData("RelativeFlywheel", relativeShooting.getEffectiveDistance());


        ActiveOpMode.telemetry().addData("flywheelVeloTarget", flywheelCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("flywheelVeloCurrent", (flywheelCurrentRPM));
//        ActiveOpMode.telemetry().addData("hoodPosTarget", hoodCalculator.getSetpoint());
//        ActiveOpMode.telemetry().addData("hoodPosCurrent", Drivebase.INSTANCE.getHoodQuadature());
    }


    public Command shootFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(getFlywheelRPM()));
    }

    public Command shootFlywheel(double flywheelRPM){
        return new InstantCommand(() -> {
            flywheelCalculator.setSetpoint(flywheelRPM);
        });
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

    public Command spinHoodZero(){
        return new InstantCommand(() -> hoodCalculator.setSetpoint(0));
    }

    public double getFlywheelRPM(){
        //Need to find new Formula for RMP based on Distance
        return 9.44207 * relativeShooting.getEffectiveDistance() + 4000;
    }

    public double getRelativeFlyWheelRPM(){
//        return 400 * (2 + relativeShooting.getv_shot());
        return 0;
    }

    public double getHoodAngle(){
        if (Drivebase.INSTANCE.getFollower().getPose().getY() < 40){
            return Constants.FlywheelConstants.flywheelVals[0][3];
        } else {
            return Constants.FlywheelConstants.flywheelVals[1][3];
        }
    }


    public Command stopHood(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(0.0))
                .requires(this);
    }







}

package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDflywheel;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.RelativeShooting;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
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
            Constants.hood_kP,
            Constants.hood_kI,
            Constants.hood_kD,
            Constants.hood_kF,
            Constants.hoodPositionToleranceRAD
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

        flywheel.setPower(flywheelCalculator.calculate(flywheelCurrentRPM));
        hood.setPower(hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

//        ActiveOpMode.telemetry().addData("flywheelPIDval", flywheelCalculator.calculate((flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR()) * 60));
//        ActiveOpMode.telemetry().addData("hoodPIDval", hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

        ActiveOpMode.telemetry().addData("Power: ", flywheelCalculator.calculate(flywheelCurrentRPM));
        ActiveOpMode.telemetry().addData("Effective Distance: ", relativeShooting.getEffectiveDistance());
        ActiveOpMode.telemetry().addData("flywheelVeloTarget: ", flywheelCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("flywheelVeloCurrent: ", (flywheelCurrentRPM));
        ActiveOpMode.telemetry().addData("flywheelOffset: ", (Constants.RelativeShootingConstants.flywheelRPM));
        ActiveOpMode.telemetry().addData("hoodPosTarget: ", hoodCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("hoodPosCurrent: ", Drivebase.INSTANCE.getHoodQuadature());
    }

    public Command shootFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(getRelativeFlyWheelRPM()));
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
        hoodCalculator.setSetpoint(testHoodAngle());
    }

    public Command spinHoodZero(){
        return new InstantCommand(() -> hoodCalculator.setSetpoint(0));
    }

    public double testFlywheelRPM(){
        return Constants.RelativeShootingConstants.flywheelRPM;
    }

    public void changeRPMMethod(double change) {
        Constants.RelativeShootingConstants.flywheelRPM += change;
    }

    public Command changeRPM(double change){
        return new InstantCommand(() -> changeRPMMethod(change));

    }

    public double getRelativeFlyWheelRPM(){
        double RPM = 16.77015 * relativeShooting.getEffectiveDistance() + 2374.80969 + Constants.RelativeShootingConstants.flywheelRPM;
        if(Drivebase.INSTANCE.getFollower().getPose().getX() > 72 + Constants.robotWidth/2 && Drivebase.INSTANCE.getFollower().getPose().getY() < 24){
            RPM -= 80;
        }
        return RPM;
    }

    public double getHoodAngle(){
        //Need to test if it works
        return 0.0103480208477 * Math.pow(relativeShooting.getEffectiveDistance(),1.24083);
    }

    public double testHoodAngle(){
        return Constants.RelativeShootingConstants.hoodAngle;
    }

    public Command changeTestHoodAngle(double change){
        return new InstantCommand(() -> changeTestHoodAngleMethod(change));
    }

    public void changeTestHoodAngleMethod(double change){
        Constants.RelativeShootingConstants.hoodAngle += change;
    }

    public Command changePID(){
        return new InstantCommand(() -> {
            hoodCalculator.setPID(
                    Constants.hood_kP,
                    Constants.hood_kI,
                    Constants.hood_kD,
                    Constants.hood_kF,
                    Constants.hoodPositionToleranceRAD);
        });
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDflywheel;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.RelativeShooting;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel1;
    private MotorEx flywheel2;

    private CRServoEx hood;

    private RelativeShooting relativeShooting = new RelativeShooting();

    private Constants.Side side;

    private boolean bool = false;



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
        flywheel1 = new MotorEx(Constants.FlywheelConstants.flywheel1Name).zeroed().reversed();
//        flywheel2 = new MotorEx(Constants.FlywheelConstants.flywheel2Name).zeroed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
    }

    @Override
    public void periodic(){
        double flywheelCurrentRPM = -flywheel1.getVelocity()/Util.GoBILDA.BARE.getCPR() * 60;
        relativeShooting.update();

        shootFlywheelMethod(bool);
        spinHood();

        flywheel1.setPower(flywheelCalculator.calculate(flywheelCurrentRPM));
//
//        .setPower(flywheelCalculatorlculator.calculate(flywheelCurrentRPM));
        hood.setPower(hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

//        ActiveOpMode.telemetry().addData("flywheelPIDval", flywheelCalculator.calculate((flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR()) * 60));
//        ActiveOpMode.telemetry().addData("hoodPIDval", hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

//        ActiveOpMode.telemetry().addData("Power: ", flywheelCalculator.calculate(flywheelCurrentRPM));
//        ActiveOpMode.telemetry().addData("Effective Distance: ", relativeShooting.getEffectiveDistance());
        ActiveOpMode.telemetry().addData("flywheelVeloTarget: ", flywheelCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("flywheelVeloCurrent: ", (flywheelCurrentRPM));
//        ActiveOpMode.telemetry().addData("flywheelOffset: ", (Constants.RelativeShootingConstants.flywheelRPM));
        ActiveOpMode.telemetry().addData("hoodPosTarget: ", hoodCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("hoodPosCurrent: ", Drivebase.INSTANCE.getHoodQuadature());
    }

    public Command shootFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(getRelativeFlyWheelRPM()));
    }

    public void shootFlywheelMethod(boolean idk){
        if(idk){
            flywheelCalculator.setSetpoint(getRelativeFlyWheelRPM());
        } else {
            flywheelCalculator.setSetpoint(0);
        }
    }

    public Command changeBool(){
        return new InstantCommand(() -> bool = !bool);
    }


    public void setSide(Constants.Side side){
        this.side = side;
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
        hoodCalculator.setSetpoint(getRelativeHoodAngle());
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
        double RPM = 16.77015 * relativeShooting.getEffectiveDistance() + 2384.80969 + Constants.RelativeShootingConstants.flywheelRPM;
        if(side == Constants.Side.BLUE){
            if(Drivebase.INSTANCE.getFollower().getPose().getX() > 72 + Constants.robotWidth/2 && Drivebase.INSTANCE.getFollower().getPose().getY() < 24){
                return RPM - 80;
            } else {
                return RPM;
            }
        } else if(side == Constants.Side.RED) {
            if(Drivebase.INSTANCE.getFollower().getPose().getX() < 72 + Constants.robotWidth/2 && Drivebase.INSTANCE.getFollower().getPose().getY() < 24){
                return RPM - 80;
            } else {
                return RPM;
            }
        } else {
            return RPM;
        }
    }

    public double getRelativeHoodAngle(){
        //Need to test if it works
        double hoodAngle = (-0.00028838 * relativeShooting.getEffectiveDistance() * relativeShooting.getEffectiveDistance())
                + (0.0969944 * relativeShooting.getEffectiveDistance())
                - 3.25772;
        if(hoodAngle < 0.5){
            hoodAngle = 0.5;
        }
        return hoodAngle;
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
                    Constants.FlywheelConstants.hood_kP,
                    Constants.FlywheelConstants.hood_kI,
                    Constants.FlywheelConstants.hood_kD,
                    Constants.FlywheelConstants.hood_kF,
                    Constants.FlywheelConstants.hoodPositionToleranceRAD);
        });
    }

    public Command changeRunRelative(){
        return new InstantCommand(() -> relativeShooting.changeRunRelativeMethod());
    }

}

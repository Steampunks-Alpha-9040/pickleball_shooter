package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.JankParser;
import org.firstinspires.ftc.teamcode.util.PIDflywheel;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.ShootingPoints;
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

    private double flywheelTarget = 3000;

    private double hoodTarget;

    private double hoodQuad;

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
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);

        hoodQuad = Constants.FlywheelConstants.hoodStartingPos;
        hoodTarget = Constants.FlywheelConstants.hoodStartingPos;

    }

    @Override
    public void periodic(){
        hoodQuad = Drivebase.INSTANCE.getHoodQuadature();

        double flywheelCurrentRPM = flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR() * 60;

        setShooting();
        setHood();

        flywheel.setPower(flywheelCalculator.calculate(flywheelCurrentRPM));
        hood.setPower(hoodCalculator.calculate(hoodQuad));

        ActiveOpMode.telemetry().addData("flywheelPIDval", flywheelCalculator.calculate((flywheel.getVelocity()/Util.GoBILDA.BARE.getCPR()) * 60));
        ActiveOpMode.telemetry().addData("hoodPIDval", hoodCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));

        ActiveOpMode.telemetry().addData("flywheelVeloTarget", flywheelCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("flywheelVeloCurrent", flywheelCurrentRPM);
        ActiveOpMode.telemetry().addData("hoodPosTarget", hoodCalculator.getSetpoint());
        ActiveOpMode.telemetry().addData("hoodPosCurrent", Drivebase.INSTANCE.getHoodQuadature());
    }

    public double[] parseLookupTable(double x, double y){
        int i = JankParser.findClosestPointIndex(x,y);

        return ShootingPoints.points[i];
    }

    public void setShooting(){
        double[] point = parseLookupTable(Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.METER), Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.METER));
        double maxtheta = point[7];
        double mintheta = point[6];
        double hoodAngle = ((maxtheta+mintheta)/2)*((float)Math.PI/180);
        double outputVelo = (point[2])*(hoodAngle*hoodAngle*hoodAngle)+(point[3])*(hoodAngle*hoodAngle)+(point[4])*(hoodAngle)+point[5];
        double flywheelVelo = ((outputVelo+2.0187818)/0.0025192438) + 1000;

        hoodTarget = hoodAngle;
        flywheelTarget = flywheelVelo;
    }


    public Command shootFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(flywheelTarget));
    }

    public Command stopFlywheel(){
        return new InstantCommand(() -> flywheelCalculator.setSetpoint(0));
    }

    public Command fasterFLywheel(){
        return new InstantCommand(() -> flywheelTarget += 100);
    }

    public Command slowerFLywheel() {
        return new InstantCommand(() -> flywheelTarget -= 100);
    }

    public void setHood(){
       hoodCalculator.setSetpoint(hoodTarget);
    }

    public Command spinHoodUp(){
        return new InstantCommand(() -> hoodTarget += 0.05);
    }

    public Command spinHoodDown(){
        return new InstantCommand(() -> hoodTarget -= 0.05);
    }

    public Command stopHood(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(0.0))
                .requires(this);
    }









}
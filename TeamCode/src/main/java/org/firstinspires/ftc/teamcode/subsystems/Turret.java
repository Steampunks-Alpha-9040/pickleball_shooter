package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDposition;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM;
    private CRServoEx turretS;

    private double turretQuad;

    private double turretTargetAngle;


    private final PIDposition controller = new PIDposition(
            Constants.TurretConstants.turret_kP,
            Constants.TurretConstants.turret_kI,
            Constants.TurretConstants.turret_kD,
            Constants.TurretConstants.turret_kF,
            Constants.TurretConstants.turretTolerance_VisionAngleRad);


    @Override
    public void initialize(){
        turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
        turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);

        turretQuad =0;

        //should be 0 it is scuffed though
    }

    @Override
    public void periodic(){
        turretQuad = Drivebase.INSTANCE.getTurretQuadature();
        turretTargetAngle = calculateTurretAngle();

        setTurret();
        moveTurret();

        ActiveOpMode.telemetry().addData("turretTargetAngle", turretTargetAngle);
        ActiveOpMode.telemetry().addData("turretEncoder", turretQuad);
    }

    public double calculateTurretAngle(){
        double offset=-Math.PI/2;
        double maxTurretAngle=1.3;
        double minTurretAngle=-1.42;
        double trueheading=(Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS)+offset+2*Math.PI+Math.PI)%(2*Math.PI)-Math.PI;

        switch (Constants.OpModeConstants.side){
            case RED:
                double redVal=Math.atan2(
                        Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH)-Constants.OpModeConstants.REDscore.getX(),
                        Constants.OpModeConstants.REDscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - trueheading;


                if(redVal>=minTurretAngle&&redVal<=maxTurretAngle){
                    return redVal;
                }else if (redVal<minTurretAngle){
                    return minTurretAngle;
                }else if (redVal>maxTurretAngle){
                    return maxTurretAngle;
                }else{
                    return 0;
                }
            case BLUE:
                double blueVal=Math.atan2(
                        Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH)-Constants.OpModeConstants.BLUEscore.getX(),
                        Constants.OpModeConstants.BLUEscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - trueheading;
//                ActiveOpMode.telemetry().addData("x pos", Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH));
//                ActiveOpMode.telemetry().addData("y pos", Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH));
                ActiveOpMode.telemetry().addData("blueval", blueVal);
                ActiveOpMode.telemetry().addData("trueheading", trueheading);
                if(blueVal>=minTurretAngle&&blueVal<=maxTurretAngle){
                    return blueVal;
                }else if (blueVal<minTurretAngle){
                    return minTurretAngle;
                }else if (blueVal>maxTurretAngle){
                    return maxTurretAngle;
                }else{
                    return 0;
                }


            default:
                return 0;
        }
    }

    public Command spinTurretRight(){
        return new InstantCommand(() -> {
            turretM.setPower(0.1);
            turretS.setPower(0.1);
        }).requires(this);
    }
    public Command spinTurretLeft(){
        return new InstantCommand(() -> {
            turretM.setPower(-0.1);
            turretS.setPower(-0.1);
        }).requires(this);
    }
    public Command stopTurret(){
        return new InstantCommand(() -> {
            turretM.setPower(0);
            turretS.setPower(0);
        }).requires(this);
    }


    private void setTurret(){
        controller.setSetpoint(turretTargetAngle);
    }

    private void moveTurret(){
        double pow = controller.calculate(turretQuad);
        turretM.setPower(-pow);
        turretS.setPower(-pow);
    }


}
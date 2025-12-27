package org.firstinspires.ftc.teamcode.util;

import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

public class AutoAimCalculator {

    public AutoAimCalculator(){

    }

    private double calculateTurretAngle(Pose2D botPos){

        switch (Constants.OpModeConstants.side){
            case RED:
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getX() - botPos.getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.BLUEscore.getY() - botPos.getY(DistanceUnit.INCH)
                ) - botPos.getHeading(AngleUnit.RADIANS);
            case BLUE:
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getX() - botPos.getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.REDscore.getY() - botPos.getY(DistanceUnit.INCH)
                ) - botPos.getHeading(AngleUnit.RADIANS);

            default:
                return 0;
        }
    }
    public double[] calculateFast(Pose2D botPos){

        int fileRow = JankParser.findClosestPointIndex(ShootingPoints.points,botPos.getX(DistanceUnit.METER),botPos.getY(DistanceUnit.METER));
        double[] closestEntry = ShootingPoints.points[fileRow];
        double hoodAngle = (((closestEntry[7] + closestEntry[6])/2)/360)*2*Math.PI;
        double flywheelRPM =
                closestEntry[2] * (hoodAngle*hoodAngle*hoodAngle) +
                        closestEntry[3] * (hoodAngle*hoodAngle) +
                        closestEntry[4] * (hoodAngle) +
                        closestEntry[5];

        flywheelRPM = (flywheelRPM+2.0187818)/0.0025192438;



        double turretAngle=calculateTurretAngle(botPos);
        return new double[]{flywheelRPM,hoodAngle,turretAngle};
    }
    public double[] calculate(Pose2D botPos,Vector2d botVelo){
        double minSpeed=0.15f;
        if(Math.sqrt((botVelo.getX()*botVelo.getX())+(botVelo.getY()*botVelo.getY()))<minSpeed){
            //returns [flywheel_speed,hood_angle,turret_angle]
            return calculateFast(botPos);
        }

        //returns [flywheel_speed,hood_angle,turret_angle]
        return new double[] {0,0,0};
    }
}

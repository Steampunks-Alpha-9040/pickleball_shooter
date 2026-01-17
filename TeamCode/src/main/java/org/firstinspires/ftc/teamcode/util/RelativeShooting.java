package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.Constants.OpModeConstants.side;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

public class RelativeShooting {
    //Velocity
    private double v_x = Drivebase.INSTANCE.getFollower().getVelocity().getXComponent();
    private  double v_y = Drivebase.INSTANCE.getFollower().getVelocity().getYComponent();

    //Position & Heading
    private double position_x = Drivebase.INSTANCE.getFollower().getPose().getX();
    private double position_y = Drivebase.INSTANCE.getFollower().getPose().getY();
    private double robotHeading = Drivebase.INSTANCE.getFollower().getPose().getHeading();

    //Math
    private double phi = calculateRobotGoalAngle();

    private double v_radial = v_y * Math.cos(phi - robotHeading) + v_x * Math.sin(phi - robotHeading);
    private double v_tangent = v_y * Math.sin(phi - robotHeading) - v_x * Math.cos(phi - robotHeading);

    private double distance = calculateRobotGoalDistance();

    private double v_shot = distance / Constants.RelativeShootingConstants.airTime - v_radial;

    private double turretTarget = phi + Math.atan2(v_tangent,v_shot) - robotHeading;

    public RelativeShooting(){

    }

    //Setters
    public void setv_shot(double update){ v_shot = update; }
    public void setTurretTarget(double update){ turretTarget = update; }

    //Getters
    public double getv_shot() { return v_shot; }
    public double getTurretTarget() { return turretTarget; }


    public void update(){
        RelativeShooting temp = new RelativeShooting();
        setv_shot(temp.getv_shot());
        setTurretTarget(temp.getTurretTarget());
    }


    public double calculateRobotGoalAngle(){
        switch (side){
            case RED:
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getY() - position_y,
                        Constants.OpModeConstants.REDscore.getX() - position_x
                );
            case BLUE:
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getY() - position_y,
                        Constants.OpModeConstants.BLUEscore.getX() - position_x
                );
            default:
                return 0;
        }
    }

    public double calculateRobotGoalDistance(){
        switch (side){
            case RED:
                double redY =Constants.OpModeConstants.REDscore.getY() - position_y;
                double redX = Constants.OpModeConstants.REDscore.getX() - position_x;
                return Math.sqrt(redY * redY + redX * redX);
            case BLUE:
                double blueY =Constants.OpModeConstants.BLUEscore.getY() - position_y;
                double blueX = Constants.OpModeConstants.BLUEscore.getX() - position_x;
                return Math.sqrt(blueY * blueY + blueX * blueX);
            default:
                return 0;
        }
    }

}

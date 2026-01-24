package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.Constants.OpModeConstants.side;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

public class RelativeShooting {

    private double distance;
    private double effectiveDistance; // required ball exit velocity
    private double turretTarget;// turret angle relative to robot

    public static boolean runRelative = true;

    public RelativeShooting() {}

    public void update() {
        com.pedropathing.follower.Follower follower = Drivebase.INSTANCE.getFollower();
        if (follower == null) return; // safety during init

        // --- Robot state (field frame) ---
        // Need to figure out units
        double vX = follower.getVelocity().getXComponent();
        double vY = follower.getVelocity().getYComponent();

        // --- Tolerance Check ---
        if(Math.abs(vX) < Constants.RelativeShootingConstants.velocityTolerance){
            vX = 0;
        }
        if(Math.abs(vY) < Constants.RelativeShootingConstants.velocityTolerance){
            vY = 0;
        }

        double posX = follower.getPose().getX();
        double posY = follower.getPose().getY();
        double heading = follower.getPose().getHeading();

        // --- Target geometry ---
        double turretPosX = posX - (Constants.RelativeShootingConstants.turretOffset * Math.cos(heading));
        double turretPosY = posY - (Constants.RelativeShootingConstants.turretOffset * Math.sin(heading));

        double phi = calculateRobotGoalAngle(turretPosX, turretPosY);
        distance = calculateRobotGoalDistance(turretPosX, turretPosY);

        // --- Decompose robot velocity ---

        double vRadial  = vX * Math.cos(phi) + vY * Math.sin(phi);
        double vTangent = -vX * Math.sin(phi) + vY * Math.cos(phi);

        double vShot = distance / Constants.RelativeShootingConstants.airTime - vRadial;


        double turretLead = Math.atan2(-vTangent, vShot);

//        if(vShot < 0){
//            Constants.RelativeShootingConstants.allowShootOnMove = false;
//        }

        // --- Flywheel + Turret + Hood Values
        // --- Effective Distance needs to be used to calculate FlywheelRPM and HoodAngle ---
        effectiveDistance = Constants.RelativeShootingConstants.airTime * Math.sqrt(vTangent * vTangent + vShot * vShot);
        effectiveDistance = Constants.RelativeShootingConstants.relativeFlywheelEffectiveness * (effectiveDistance - distance) + distance;

        turretLead = Math.max(
                -Constants.RelativeShootingConstants.maxTurretAdjustment,
                Math.min(Constants.RelativeShootingConstants.maxTurretAdjustment, turretLead)
        );

        turretTarget = phi + turretLead - heading;
        turretTarget = Math.atan2(Math.sin(turretTarget),Math.cos(turretTarget));

        if (vShot <= 0 || !runRelative) {
            turretTarget = phi - heading;
            effectiveDistance = distance;
        }
    }

    public double getDistance(){
        return distance;
    }
    public double getEffectiveDistance() {
        return effectiveDistance;
    }

    public double getTurretTarget() {
        return turretTarget;
    }

    public static void changeRunRelativeMethod(){
        runRelative = !runRelative;
    }

    private double calculateRobotGoalAngle(double posX, double posY) {
        double offsetX = 0;
        double offsetY = 0;
        switch (side) {
            case RED:
                if(posX < 72 && posY > 72){
                    offsetX = 6;
                    offsetY = 6;
                }
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getY() + offsetY - posY,
                        Constants.OpModeConstants.REDscore.getX() + offsetX - posX
                );
            case BLUE:
                if(posX > 72 && posY > 72){
                    offsetX = -8;
                    offsetY = 8;
                }
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getY() + offsetY - posY,
                        Constants.OpModeConstants.BLUEscore.getX() + offsetX - posX
                );
            default:
                return 0.0;
        }
    }

    private double calculateRobotGoalDistance(double posX, double posY) {
        double dx, dy;

        switch (side) {
            case RED:
                dx = Constants.OpModeConstants.REDscore.getX() - posX;
                dy = Constants.OpModeConstants.REDscore.getY() - posY;
                break;
            case BLUE:
                dx = Constants.OpModeConstants.BLUEscore.getX() - posX;
                dy = Constants.OpModeConstants.BLUEscore.getY() - posY;
                break;
            default:
                return 0.0;
        }

        return Math.hypot(dx, dy);
    }
}
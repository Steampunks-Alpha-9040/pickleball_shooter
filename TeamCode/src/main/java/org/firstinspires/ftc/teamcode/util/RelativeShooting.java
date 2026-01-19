package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.Constants.OpModeConstants.side;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

public class RelativeShooting {

    private double effectiveDistance; // required ball exit velocity
    private double turretTarget;  // turret angle relative to robot

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
        double distance = calculateRobotGoalDistance(turretPosX, turretPosY);
//3.097
        // Angle from robot forward to goal
        double dTheta = phi - heading;

        // --- Decompose robot velocity ---
        double vRadial = vY * Math.cos(dTheta) - vX * Math.sin(dTheta);

        double vTangent = vY * Math.sin(dTheta) + vX * Math.cos(dTheta);

        double vShot = distance / Constants.RelativeShootingConstants.airTime - vRadial;

//        if(vShot < 0){
//            Constants.RelativeShootingConstants.allowShootOnMove = false;
//        }

        // --- Flywheel + Turret + Hood Values
        // --- Effective Distance needs to be used to calculate FlywheelRPM and HoodAngle ---
        effectiveDistance = Constants.RelativeShootingConstants.airTime * Math.sqrt(vTangent * vTangent + vShot * vShot);

        turretTarget = phi + Math.atan2(vTangent, vShot) - heading;
        turretTarget = Math.atan2(Math.sin(turretTarget),Math.cos(turretTarget));
    }

    // ---------------- GETTERS ----------------

    public double getEffectiveDistance() {
        return effectiveDistance;
    }

    public double getTurretTarget() {
        return turretTarget;
    }

    // ---------------- HELPERS ----------------

    private double calculateRobotGoalAngle(double posX, double posY) {
        switch (side) {
            case RED:
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getY() - posY,
                        Constants.OpModeConstants.REDscore.getX() - posX
                );
            case BLUE:
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getY() - posY,
                        Constants.OpModeConstants.BLUEscore.getX() - posX
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
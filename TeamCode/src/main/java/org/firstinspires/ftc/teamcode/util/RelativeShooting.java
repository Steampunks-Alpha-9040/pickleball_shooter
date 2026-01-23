package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.teamcode.Constants.OpModeConstants.side;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

public class RelativeShooting {

    private double flywheelRPM;
    private double turretTarget;

    private double hoodTarget;

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

        double theta = calculateRobotGoalAngle(turretPosX, turretPosY);
        double distance = calculateRobotGoalDistance(turretPosX, turretPosY);
//3.097
        double flywheelRPMDesire = getRelativeFlyWheelRPM(distance);
        double vDesire = flywheelRPMDesire / 400 - 2;
        double phi = Math.toRadians(-4.14286 * getRelativeHoodAngle(distance) + 68.85714);

        double vShotX = vDesire * Math.cos(theta) * Math.cos(phi) - vX;
        double vShotY = vDesire * Math.sin(theta) * Math.cos(phi) - vY;
        double vShotZ = vDesire * Math.sin(phi);

        double vShotMag = Math.sqrt(vShotX * vShotX + vShotY * vShotY + vShotZ * vShotZ);

        // required ball exit velocity
        flywheelRPM = 400 * vShotMag + 800;

        turretTarget = Math.atan2(vShotX, vShotY) + Math.PI - heading;
        turretTarget = Math.atan2(Math.sin(turretTarget), Math.cos(turretTarget));

        hoodTarget = Math.atan2(Math.sqrt(vShotX * vShotX + vShotY * vShotY), vShotZ);
        hoodTarget = -0.232745 * hoodTarget + 16.11557;
        if(hoodTarget < 0){
            hoodTarget = 0;
        } else if (hoodTarget > 5){
            hoodTarget = 4.6;
        }
        

    }

    // ---------------- GETTERS ----------------

    public double getFlywheelRPM() {
        return flywheelRPM;
    }

    public double getTurretTarget() {
        return turretTarget;
    }

    public double getHoodTarget() {
        return hoodTarget;
    }

    // ---------------- HELPERS ----------------

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

    public double getRelativeFlyWheelRPM(double distance){
        double RPM = 16.77015 * distance + 2374.80969 + Constants.RelativeShootingConstants.flywheelRPM;
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

    public double getRelativeHoodAngle(double distance){
        //Need to test if it works
        return (-0.00028838 * distance * distance)
                + (0.0969944 * distance)
                - 3.25772;
    }
}
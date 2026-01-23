package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static Vision INSTANCE = new Vision();
    private Limelight3A limelight;

    private double x;
    private double y;
    private double heading;

    private boolean poseValid = false;

    @Override
    public void initialize() {
        INSTANCE = this;

        limelight = ActiveOpMode.hardwareMap()
                .get(Limelight3A.class, "limelight");

        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(10);
        limelight.start();
    }

    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();

        if (result == null || !result.isValid()) {
            poseValid = false;
            return;
        }

        Pose3D botpose = result.getBotpose();
        if (botpose == null) {
            poseValid = false;
            return;
        }

        // flywheel - limelight 37.4475 mm
        //flywheel to center 109.22579 mm
        //center to robot 78.65994 mm

        double robotHeading = Drivebase.INSTANCE.getFollower().getHeading();
        double turretHeading = Drivebase.INSTANCE.getTurretQuadature();

        //X and X Flipped, Convert to Inch, Fix (0,0), Add Turret Offset, Add Limelight Offset
        //Check the sign of turretHeading but it should be the sum assuming CC is positive and C is negative
        x = botpose.getPosition().y * 39.3701
                + 72
                + Constants.VisionConstants.turretToCenter * Math.cos(robotHeading)
                - Constants.VisionConstants.flywheelToTurret * Math.cos(robotHeading - turretHeading)
                + Constants.VisionConstants.limeLighttoFlywheel * Math.sin(robotHeading - turretHeading)
        ;
        y = -botpose.getPosition().x * 39.3701
                + 72
                + Constants.VisionConstants.turretToCenter * Math.sin(robotHeading)
                - Constants.VisionConstants.flywheelToTurret * Math.sin(robotHeading - turretHeading)
                - Constants.VisionConstants.limeLighttoFlywheel * Math.cos(robotHeading - turretHeading)
        ;
        heading = (Math.toRadians(botpose.getOrientation().getYaw()) - Math.PI/2) + turretHeading;
        heading = Math.atan2(Math.sin(heading), Math.cos(heading));

        poseValid = true;

        ActiveOpMode.telemetry().addData("turretHeading: ", turretHeading);

        ActiveOpMode.telemetry().addData("VisionValid: ", hasValidPose());
        ActiveOpMode.telemetry().addData("VisionX: ", getX());
        ActiveOpMode.telemetry().addData("VisionXRaw: ", botpose.getPosition().x);
        ActiveOpMode.telemetry().addData("VisionY: ", getY());
        ActiveOpMode.telemetry().addData("VisionYRaw: ", botpose.getPosition().y);
        ActiveOpMode.telemetry().addData("VisionHeading: ", Math.toDegrees(getHeading()));
        ActiveOpMode.telemetry().addData("VisionHeadingRaw: ", Math.toRadians(botpose.getOrientation().getYaw()));

    }

    public boolean hasValidPose() {
        return poseValid;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeading() {
        return heading;
    }

    public Pose getPose(){
        return new Pose(getX(), getY(), getHeading());
    }
}
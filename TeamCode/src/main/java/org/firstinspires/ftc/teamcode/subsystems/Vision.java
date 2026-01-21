package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static Vision INSTANCE;
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

        //Converting to Inches
        x = botpose.getPosition().x * 39.3701;
        y = botpose.getPosition().y * 39.3701;
        heading = Math.toRadians(botpose.getOrientation().getYaw());

        poseValid = true;
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
}
package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Point;

import java.util.Optional;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static Vision INSTANCE = new Vision();
    private Limelight3A limelight;

    private Pose2D cameraRobotPose;

    private double visionSigma;


    public void initialize(){
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(10);

        limelight.start();
    }

    public void periodic(){
        limelight.updateRobotOrientation(Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.DEGREES));
        Pose2D prevPose = cameraRobotPose;
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
                visionSigma = result.getBotposeAvgArea() * (1.0 + Math.min(1, Math.abs(Drivebase.INSTANCE.getTurretQuadature()/(Math.PI/2))));
                Pose3D botpose = result.getBotpose();

                // 4. Rotate turret→camera based on turret angle

                Vector2d turretToCamRotated = Constants.VisionConstants.cameraToTurretCenter.rotateBy(Drivebase.INSTANCE.getTurretQuadature());

                // Total transform robot→camera
                Vector2d robotToCamera = Constants.VisionConstants.turretCenterToRobotCenter.plus(turretToCamRotated);

                // 5. Now compute robot pose = cameraPose minus robot→camera
                double robotX = botpose.getPosition().x - robotToCamera.getX();
                double robotY = botpose.getPosition().y - robotToCamera.getY();

                cameraRobotPose = new Pose2D(DistanceUnit.METER,robotX, robotY, AngleUnit.RADIANS, Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS));
        }
        if (prevPose == cameraRobotPose){
            cameraRobotPose = new Pose2D(DistanceUnit.METER, 0, 0, AngleUnit.RADIANS, 0);
        }
    }

    public double getVisionSigma(){
        return visionSigma;
    }

    public Pose2D getPose2d(){
        return cameraRobotPose;
    }

}

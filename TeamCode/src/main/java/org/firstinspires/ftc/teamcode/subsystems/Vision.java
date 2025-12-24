package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static Vision INSTANCE = new Vision();
    private Limelight3A limelight;

    private Pose2D cameraRobotPose;

    private double visionSigma;

    private LLResult prevResult;


    public void initialize(){
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(10);

        limelight.start();
    }

    public void periodic(){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && result != prevResult) { //checks if result is null, if it's valid, and if it's different than before
//            ActiveOpMode.telemetry().addData("raw cam X", result.getBotpose().getPosition().x);
//            ActiveOpMode.telemetry().addData("raw cam Y", result.getBotpose().getPosition().y);
//            ActiveOpMode.telemetry().addData("raw cam area", result.getBotposeAvgArea());

            Pose3D botpose = result.getBotpose();

            Vector2d cameraToTurret = Constants.VisionConstants.cameraToTurretCenter.rotateBy(Angle.fromRad(Drivebase.INSTANCE.getTurretQuadature()).inDeg);
//
            Vector2d robotToCamera = Constants.VisionConstants.turretCenterToRobotCenter.plus(cameraToTurret);

            Vector2d cameraTranslated = new Vector2d(botpose.getPosition().x, botpose.getPosition().y).plus(robotToCamera).rotateBy(-90).plus(new Vector2d(1.8288, 1.8288));
            if (result.getBotposeAvgArea() > 0.4){
                if (Util.isNear(Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.METER), cameraTranslated.getX(), 0.3) &&
                        Util.isNear(Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.METER), cameraTranslated.getY(), 0.3)){

                    visionSigma = result.getBotposeAvgArea() * (1.0 + Math.min(1, Math.abs(Drivebase.INSTANCE.getTurretQuadature()/(Math.PI))));



                    // Total transform robot→camera


                    cameraRobotPose = new Pose2D(DistanceUnit.METER, cameraTranslated.getX(), cameraTranslated.getY(), AngleUnit.RADIANS, Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS));



                    return;
                }
            }
            prevResult = result;
        }
        cameraRobotPose = null;
    }

    public double getVisionSigma(){
        return visionSigma;
    }


    public Pose2D getRaw2D(){
        return cameraRobotPose;
    }

}

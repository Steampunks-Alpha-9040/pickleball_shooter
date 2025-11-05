package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.teamcode.Constants;

import java.util.Optional;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static final Vision INSTANCE = new Vision();


    private Limelight3A limelight;

    private Optional<LLResult> currentResult;
    private Optional<LLResult> previousResult;

    private TurretDirection turretDirection = TurretDirection.STOP;
    private double targetDistance = 0.0;


    public enum TurretDirection{
        LEFT, RIGHT, STOP
    }


    @Override
    public void initialize(){

        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, Constants.VisionConstants.limelight);

        limelight.setPollRateHz(100);
        limelight.start();
    }

    @Override
    public void periodic(){
        currentResult = filterLimelightInput(limelight.getLatestResult());
        if (currentResult.isPresent()){
            turretDirection = updateTurretDirection();
            targetDistance = updateTargetDistance();
            previousResult = currentResult;
        }
    }


    private Optional<LLResult> filterLimelightInput(LLResult input){
        //checks if the input is valid
        if (!input.isValid()){
            return Optional.empty();
        }
        //checks if we see more than 1 apriltags, if we see more uhhh that's bad

        if (input.getBotposeTagCount() > 2){
            return Optional.empty();
        }

            //checks if the latency is way too long
        if (input.getCaptureLatency() > Constants.VisionConstants.accepted_pipeline_latency_ms){
            return Optional.empty();
        }
        //checks if the output is wildly off in the z-axis
        if (input.getBotpose().getPosition().z > Constants.VisionConstants.accepted_y_offset){
            return Optional.empty();
        }
        //First checks if we need to check the yaw change amount, then we check if the yaw change is
        //great enough that we need to discard the input
        if (input.getBotposeAvgArea() < Constants.VisionConstants.kTagAreaThresholdForYawCheck
                && previousResult.isPresent()) {
            double yawDiff =
                    Math.abs(previousResult.get().getBotpose().getOrientation().getYaw()
                                - input.getBotpose().getOrientation().getYaw());

            if (yawDiff > Constants.VisionConstants.kDefaultYawDiffThreshold) {
                return Optional.empty();
            }
        }

        return Optional.of(input);
    }

    private TurretDirection updateTurretDirection(){
        if (currentResult.get().getTx() > Constants.TurretConstants.turretTolerance_VisionAngleDeg){
            return TurretDirection.RIGHT;
        } else if (currentResult.get().getTx() < -Constants.TurretConstants.turretTolerance_VisionAngleDeg) {
            return TurretDirection.LEFT;
        }
        return TurretDirection.STOP;
    }

    //todo: need to add equation for throwing
    private double updateTargetDistance(){
        return currentResult.get().getFiducialResults().get(0).getTargetXPixels();
    }

    public TurretDirection getTurretDirection(){
        return turretDirection;
    }

    public double getTurretDistance(){
        return targetDistance;
    }



}

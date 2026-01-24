package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class SixSevenFilter implements Subsystem {

    public static SixSevenFilter INSTANCE = new SixSevenFilter();

    public SixSevenFilter(){

    }

    private Timer jankTimer;
    //Expo Filter Constant
    private static final double ALPHA = 0.014;

    //Outliers
    private static final double MAX_POSITION_ERROR = 10;
    private static final double MAX_HEADING_ERROR = Math.toRadians(20);

    private double emaX = 0.0;
    private double emaY = 0.0;
    private double emaHeading = 0.0;

    Pose pedroPose;
    Pose visionPose;
    Pose filteredPose;

    @Override
    public void initialize(){
        jankTimer = new Timer();
    }

    @Override
    public void periodic(){
        ActiveOpMode.telemetry().addData("emaX: ", emaX);
        ActiveOpMode.telemetry().addData("emaY: ", emaY);
        ActiveOpMode.telemetry().addData("emaHeading: ", emaHeading);

        if (jankTimer.getElapsedTimeSeconds() >= 1.5){
            jankTimer.resetTimer();
            reset();
        }

        pedroPose = Drivebase.INSTANCE.getPastPose(Vision.INSTANCE.getVisionLatency());
        if(Vision.INSTANCE.hasValidPose()){
            visionPose = Vision.INSTANCE.getPose();
            filteredPose = expoFilterPose(pedroPose, visionPose);
            if(Drivebase.INSTANCE.getFollower().getVelocity().getMagnitude() < 3){
                correctPose(filteredPose);
            }
        } else {
            reset();
        }
    }

    public Pose expoFilterPose(Pose odoPose, Pose visionPose) {

        //Error
        double dx = visionPose.getX() - odoPose.getX();
        double dy = visionPose.getY() - odoPose.getY();
        double dTheta = angleError(visionPose.getHeading(), odoPose.getHeading());

        // Outlier rejection
        if (Math.hypot(dx, dy) > MAX_POSITION_ERROR) {
            return new Pose(0,0,0);
        }

        if (Math.abs(dTheta) > MAX_HEADING_ERROR) {
            return new Pose(0,0,0);
        }

        // Exponential moving average on the error
        emaX = (1 - ALPHA) * emaX + ALPHA * dx;
        emaY = (1 - ALPHA) * emaY + ALPHA * dy;
        emaHeading = (1 - ALPHA) * emaHeading + ALPHA * dTheta;


        return new Pose(emaX, emaY, emaHeading);
    }

    public void correctPose(Pose filteredPose){
        double fixedX = Drivebase.INSTANCE.getFollower().getPose().getX() + filteredPose.getX();
        double fixedY = Drivebase.INSTANCE.getFollower().getPose().getY() + filteredPose.getY();
        double fixedTheta = Drivebase.INSTANCE.getFollower().getPose().getHeading() + filteredPose.getHeading();
        fixedTheta = Math.atan2(Math.sin(fixedTheta), Math.cos(fixedTheta));
        Drivebase.INSTANCE.getFollower().setPose(new Pose( fixedX,fixedY,fixedTheta));
    }

    public void reset() {
        emaX = 0.0;
        emaY = 0.0;
        emaHeading = 0.0;
    }


    private double angleError(double target, double current) {
        double err = target - current;
        return Math.atan2(Math.sin(err), Math.cos(err));
    }
}
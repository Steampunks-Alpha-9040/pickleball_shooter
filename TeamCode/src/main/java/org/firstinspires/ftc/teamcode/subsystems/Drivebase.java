package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;


public class Drivebase implements Subsystem {

    public static final Drivebase INSTANCE = new Drivebase();
    private Drivebase(){}
    public static Follower follower;

    private MotorEx FL;
    private MotorEx FR;
    private MotorEx BL;
    private MotorEx BR;
    private GoBildaPinpointDriver imu;
    private Pose2D botpose;

    private double gyroOffset;

    public void initialize(){
        FL = new MotorEx(Constants.DrivebaseConstants.FL).brakeMode().reversed();
        FR = new MotorEx(Constants.DrivebaseConstants.FR).brakeMode();
        BL = new MotorEx(Constants.DrivebaseConstants.BL).brakeMode().reversed();
        BR = new MotorEx(Constants.DrivebaseConstants.BR).brakeMode();
        imu = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, Constants.DrivebaseConstants.IMU);
        imu.recalibrateIMU();

        imu.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        imu.setOffsets(-0.215,-6.766, DistanceUnit.INCH);
        imu.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED
        );
        imu.setYawScalar(Constants.DrivebaseConstants.yawScalar);

    }

    public void periodic(){
        imu.update();
        updateBotpose();
        ActiveOpMode.telemetry().addData("pose", Util.poseUnitConvertor(DistanceUnit.INCH, getBotpose()));
        ActiveOpMode.telemetry().addData("headingraw", imu.getHeading(AngleUnit.DEGREES));
    }

    public MecanumDriverControlled getMecanumDriver(){
        return new MecanumDriverControlled(
            FL, FR, BL, BR,
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX(),
            new FieldCentric(() -> Angle.fromRad(imu.getHeading(AngleUnit.RADIANS) - gyroOffset))
        );
    }

    public Command driveForwardSimple(double power, double seconds) {
        return new SequentialGroup(
                new LambdaCommand("driveForward")
                        .requires(this)
                        .setStart(() -> {
                            FL.setPower(power);
                            FR.setPower(power);
                            BL.setPower(power);
                            BR.setPower(power);
                        }),
                new Delay(seconds),
                new LambdaCommand("stopDrive")
                        .setStart(() -> {
                            FL.setPower(0);
                            FR.setPower(0);
                            BL.setPower(0);
                            BR.setPower(0);
                        })
        );
    }

    private void updateBotpose(){
        Pose2D cameraPose = Vision.INSTANCE.getRaw2D();

        double sigmaK = Constants.DrivebaseConstants.constantSigmaOdo / (Constants.DrivebaseConstants.constantSigmaOdo + Vision.INSTANCE.getVisionSigma());

        if (cameraPose == null){
            return;
        }
        double fusedX = (imu.getPosX(DistanceUnit.INCH) + (sigmaK * cameraPose.getX(DistanceUnit.INCH)))/(1+sigmaK);
        double fusedY = (imu.getPosY(DistanceUnit.INCH) + (sigmaK * cameraPose.getY(DistanceUnit.INCH)))/(1+sigmaK);
        imu.setPosition(new Pose2D(DistanceUnit.INCH, fusedX, fusedY, AngleUnit.RADIANS, imu.getHeading(AngleUnit.RADIANS)));
    }

    public void setStartingPose(double x, double y){
        imu.setPosition(new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, 0));
    }


    //We do this since the quadature is attached to the FL motor, and quadatures are only implemented for motors. We use servos for the turret :)
    public double getTurretQuadature(){
        return -((FL.getCurrentPosition()/4096)/(Constants.TurretConstants.encoderToTurret)*2*Math.PI);
    }

    public double getHoodQuadature(){
        return (((((BR.getCurrentPosition()/4096)*2*Math.PI))*((double) 16 /265))+Constants.FlywheelConstants.hoodStartingPos);
        //0.51696652 for calculated,0.53756141 for experimental
        //(BR.getCurrentPosition()/4096)*2*Math.PI
        //Big Gear Teeth:265
        //Small Gear Teeth:16
    }

    public Command zeroGryo(){
        return new InstantCommand(() -> gyroOffset = imu.getHeading(AngleUnit.RADIANS));
    }

    public Pose2D getBotpose(){
        return imu.getPosition();
    }

    public void zeroTurretQuadature(){
        FL.setCurrentPosition(0);
    }
    public void zeroHoodQuadature(){BR.setCurrentPosition(0);}


}

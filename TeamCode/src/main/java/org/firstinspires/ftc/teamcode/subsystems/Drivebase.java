package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants;

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
        imu.setOffsets(6.766,-0.215, DistanceUnit.INCH);
        imu.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED
        );
        imu.setYawScalar(1.55);
        imu.resetPosAndIMU();
        FL.atPosition(0);
    }

    public void periodic(){
        imu.update();
        ActiveOpMode.telemetry().addData("pose", getBotpose());
        updateBotpose();
        imu.setPosition(getBotpose());
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
        Pose2D cameraPose = Vision.INSTANCE.getPose2d();

        double sigmaK = Constants.DrivebaseConstants.constantSigmaOdo / (Constants.DrivebaseConstants.constantSigmaOdo + Vision.INSTANCE.getVisionSigma());

//        if (cameraPose == null){
        botpose = imu.getPosition();
//            return;
//        }
//        double fusedX = (imu.getPosX(DistanceUnit.INCH) + (sigmaK * cameraPose.getX(DistanceUnit.INCH)))/(1+sigmaK);
//        double fusedY = (imu.getPosY(DistanceUnit.INCH) + (sigmaK * cameraPose.getY(DistanceUnit.INCH)))/(1+sigmaK);
//        botpose =  new Pose2D(DistanceUnit.INCH, fusedX, fusedY, AngleUnit.RADIANS, imu.getHeading(AngleUnit.RADIANS));
    }

    public void setStartingPose(double x, double y){
        imu.setPosition(new Pose2D(DistanceUnit.INCH, x, y, AngleUnit.DEGREES, 0));
    }


    //We do this since the quadature is attached to the FL motor, and quadatures are only implemented for motors. We use servos for the turret :)
    public double getTurretQuadature(){
        return (((FL.getRawTicks()*Constants.TurretConstants.encoderToTurret)/4096)*2*Math.PI);
    }

    public Command zeroGryo(){
        return new InstantCommand(() -> gyroOffset = imu.getHeading(AngleUnit.RADIANS));
    }

    public Pose2D getBotpose(){
        return botpose;
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
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
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;


public class Drivebase implements Subsystem {

    public static final Drivebase INSTANCE = new Drivebase();
    private Drivebase(){}
    public Follower follower;

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
    }

    public void periodic(){
        follower.update();
        ActiveOpMode.telemetry().addData("pose", follower.getPose());
    }

    public PedroDriverControlled getMecanumDriver(){
        return new PedroDriverControlled(
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().rightStickX(),
                false
        );
    }

    //We do this since the quadature is attached to the FL motor, and quadatures are only implemented for motors. We use servos for the turret :)
    public double getTurretQuadature(){
        return -((FL.getCurrentPosition()/4096)/(Constants.TurretConstants.encoderToTurret)*2*Math.PI);
    }

    public double getHoodQuadature(){
        return (BR.getCurrentPosition()/4096)*2*Math.PI;
    }

    public Command zeroGryo(){
        return new InstantCommand(() -> gyroOffset = follower.getHeading());
    }

    public Command resetPose(){
        return new InstantCommand(() -> getFollower().setStartingPose(new Pose(135.7,8.69,Math.PI)));
    }

    public void setFollower(Follower follower){
        this.follower = follower;
    }

    public Follower getFollower(){
        return follower;
    }

    public Command setPose(Pose pose){
        return new InstantCommand(()-> follower.setStartingPose(pose));
    }

    public void zeroTurretQuadature(){
        FL.setCurrentPosition(0);
    }
    public void zeroHoodQuadature(){
        BR.setCurrentPosition(0);
    }


}

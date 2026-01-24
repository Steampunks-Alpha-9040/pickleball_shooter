package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.Side.BLUE;
import static org.firstinspires.ftc.teamcode.Constants.Side.RED;

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


    public MotorEx getBR(){ return BR; }
    public MotorEx getFL(){ return FL; }

    public void initialize(){
        FL = new MotorEx(Constants.DrivebaseConstants.FL).brakeMode().reversed();
        FR = new MotorEx(Constants.DrivebaseConstants.FR).brakeMode();
        BL = new MotorEx(Constants.DrivebaseConstants.BL).brakeMode().reversed();
        BR = new MotorEx(Constants.DrivebaseConstants.BR).brakeMode();
        BR.setCurrentPosition(0);
    }

    public void periodic(){
        follower.update();
        ActiveOpMode.telemetry().addData("Pose: ", follower.getPose());
        ActiveOpMode.telemetry().addData("x velocity: ", follower.getVelocity().getXComponent());
        ActiveOpMode.telemetry().addData("y velocity: ", follower.getVelocity().getYComponent());
    }


    public PedroDriverControlled getMecanumDriver(Constants.Side selectedSide){
        if (selectedSide == BLUE) {
            return new PedroDriverControlled(
                    Gamepads.gamepad1().leftStickY(),
                    Gamepads.gamepad1().leftStickX(),
                    Gamepads.gamepad1().rightStickX().negate(),
                    false
            );
        } else if(selectedSide == RED){
            return new PedroDriverControlled(
                    Gamepads.gamepad1().leftStickY().negate(),
                    Gamepads.gamepad1().leftStickX().negate(),
                    Gamepads.gamepad1().rightStickX().negate(),
                    false
            );
        } else {
            return new PedroDriverControlled(
                    Gamepads.gamepad1().leftStickY(),
                    Gamepads.gamepad1().leftStickX(),
                    Gamepads.gamepad1().rightStickX().negate(),
                    false
            );
        }

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

    public Command resetPose(int side){
        //0 is Blue
        if(side == 0){
            return new InstantCommand(() -> getFollower().setPose(new Pose(Constants.robotLength/2,Constants.robotWidth/2,Math.PI)));
        } else {
            return new InstantCommand(() -> getFollower().setPose(new Pose(144 - Constants.robotLength/2, Constants.robotWidth/2, 0)));
        }
    }

    public void setFollower(Follower follower){
        this.follower = follower;
    }

    public Follower getFollower(){
        return follower;
    }

    public Command setStartingPose(Pose pose){
        return new InstantCommand(()-> follower.setStartingPose(pose));
    }

    public void zeroTurretQuadature(){
        FL.setCurrentPosition(0);
    }
    public void zeroHoodQuadature(){
        BR.setCurrentPosition(0);
    }



}

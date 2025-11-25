package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.HolonomicMode;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.IMUEx;
import kotlin.Unit;


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


    public void initialize(){
        FL = new MotorEx(Constants.DrivebaseConstants.FL).brakeMode().reversed();
        FR = new MotorEx(Constants.DrivebaseConstants.FR).brakeMode();
        BL = new MotorEx(Constants.DrivebaseConstants.BL).brakeMode().reversed();
        BR = new MotorEx(Constants.DrivebaseConstants.BR).brakeMode();
        imu = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, Constants.DrivebaseConstants.IMU);
        imu.setOffsets(0.215,-6.766, DistanceUnit.INCH);
    }

    public void periodic(){
        botpose = imu.getPosition();
        ActiveOpMode.telemetry().addData("pose", this::getBotpose);
        FL.atPosition(0);
    }

    public MecanumDriverControlled getMecanumDriver(){
        return new MecanumDriverControlled(
            FL, FR, BL, BR,
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX(),
            new FieldCentric(() -> Angle.fromRad(imu.getHeading(AngleUnit.RADIANS)))
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

    public Pose2D getBotpose(){
        return botpose;
    }



    public double updateTurretQuadature(){
        return (FL.getRawTicks()/4096)*360;
    }

}

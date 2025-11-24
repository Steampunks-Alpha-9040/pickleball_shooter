package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

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
    private Drivebase() { }
    public static Follower follower;

    private MotorEx FL = new MotorEx(Constants.DrivebaseConstants.FL).brakeMode().reversed();
    private MotorEx FR = new MotorEx(Constants.DrivebaseConstants.FR).brakeMode();
    private MotorEx BL = new MotorEx(Constants.DrivebaseConstants.BL).brakeMode().reversed();
    private MotorEx BR = new MotorEx(Constants.DrivebaseConstants.BR).brakeMode();
//    private IMUEx imu = new IMUEx(Constants.DrivebaseConstants.IMU, Direction.DOWN, Direction.FORWARD).zeroed();


    public void initialize(){
        FL.atPosition(0);
    }

    public MecanumDriverControlled getMecanumDriver(){
        return new MecanumDriverControlled(
            FL, FR, BL, BR,
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX()
//                ,
//            new FieldCentric(imu)
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

    public double updateTurretQuadature(){
        return (FL.getRawTicks()/4096)*360;
    }

}

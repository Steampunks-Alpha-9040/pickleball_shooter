package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.core.units.Angle;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.driving.FieldCentric;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;


public class Drivebase implements Subsystem {

    public static final Drivebase INSTANCE = new Drivebase();
    private Drivebase() { }
    public static Follower follower;

    private MotorEx FL;
    private MotorEx FR;
    private MotorEx BL;
    private MotorEx BR;
    private GoBildaPinpointDriver imu;


    @Override
    public void initialize(){
        FL = new MotorEx(Constants.DrivebaseConstants.FL).brakeMode().reversed();
        FR = new MotorEx(Constants.DrivebaseConstants.FR).brakeMode();
        BL = new MotorEx(Constants.DrivebaseConstants.BL).brakeMode().reversed();
        BR = new MotorEx(Constants.DrivebaseConstants.BR).brakeMode();
        imu = ActiveOpMode.hardwareMap().get(GoBildaPinpointDriver.class, Constants.DrivebaseConstants.IMU);
    }

    public MecanumDriverControlled getFieldMecanumDriver(){
        return new MecanumDriverControlled(
            FL, FR, BL, BR,
            Gamepads.gamepad1().leftStickY().negate(),
            Gamepads.gamepad1().leftStickX(),
            Gamepads.gamepad1().rightStickX(),
            new FieldCentric(() -> Angle.fromDeg(imu.getHeading(AngleUnit.DEGREES)))
        );
    }
    public MecanumDriverControlled getRobotMecanumDriver(){
        return new MecanumDriverControlled(
                FL, FR, BL, BR,
                Gamepads.gamepad1().leftStickY().negate(),
                Gamepads.gamepad1().leftStickX(),
                Gamepads.gamepad1().rightStickX()
            );
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.teamcode.util.Mecanum;
import com.pedropathing.ftc.localization.constants.PinpointConstants;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.programs.MainOp;
import org.firstinspires.ftc.teamcode.util.PinpointLocalizer;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.GamepadEx;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.hardware.impl.Direction;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.IMUEx;


public class Drivebase extends Mecanum implements Subsystem {

    public static final Drivebase INSTANCE = new Drivebase();
    private Drivebase() {
        super(ActiveOpMode.hardwareMap());
    }
    private static Follower follower;
    private Telemetry telemetry;

    private IMUEx imu;

    private Supplier<Boolean> isSlowed = () -> ActiveOpMode.gamepad1().a;


    @Override
    public void initialize(){
        imu = new IMUEx(Constants.DrivebaseConstants.IMU, Direction.DOWN, Direction.FORWARD).zeroed();
        telemetry = ActiveOpMode.telemetry();
        follower = new Follower(new FollowerConstants(),
                                new PinpointLocalizer(ActiveOpMode.hardwareMap()),
                                this
                );
    }

    public void drive(GamepadEx gamepadEx) {
        follower.setTeleOpDrive(
                gamepadEx.leftStickX().get() * (isSlowed.get() ? Constants.DrivebaseConstants.slowScalar : 1),
                gamepadEx.leftStickY().get() * (isSlowed.get() ? Constants.DrivebaseConstants.slowScalar : 1),
                gamepadEx.rightStickX().get() * (isSlowed.get() ? Constants.DrivebaseConstants.slowScalar : 1),
                false
        );
        follower.update();
    }

    public Command controllerDrive(){
        return new LambdaCommand("DriveController")
                .requires(this)
                .setStart(() -> drive(Gamepads.gamepad1()))
                .setIsDone(() -> false);

    }

}

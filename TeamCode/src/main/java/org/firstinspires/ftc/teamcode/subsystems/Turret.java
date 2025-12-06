package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDcontroller;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM;
    private CRServoEx turretS;

    private double turretQuad;

    private double turretTargetAngle;


    private PIDcontroller controller = new PIDcontroller(
            Constants.TurretConstants.turret_kP,
            Constants.TurretConstants.turret_kI,
            Constants.TurretConstants.turret_kD,
            Constants.TurretConstants.turret_kF,
            Constants.TurretConstants.turretTolerance_VisionAngleRad);


    @Override
    public void initialize(){
        turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
        turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);

        turretQuad = 0;
    }

    @Override
    public void periodic(){
        turretQuad = Drivebase.INSTANCE.getTurretQuadature();
        turretTargetAngle = calculateTurretAngle();
        double pow = controller.calculate(turretQuad);
        turretM.setPower(-pow);
        turretS.setPower(-pow);
        ActiveOpMode.telemetry().addData("targetAngle", turretTargetAngle);
        ActiveOpMode.telemetry().addData("turretEncoder", turretQuad);
    }

    public double calculateTurretAngle(){
        switch (Constants.OpModeConstants.side){
            case RED:
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.BLUEscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS);
            case BLUE:
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.REDscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS);

            default:
                return 0;
        }
    }

    public Command spinTurretRight(){
        return new InstantCommand(() -> {
            turretM.setPower(0.1);
            turretS.setPower(0.1);
        }).requires(this);
    }
    public Command spinTurretLeft(){
        return new InstantCommand(() -> {
            turretM.setPower(-0.1);
            turretS.setPower(-0.1);
        }).requires(this);
    }
    public Command stopTurret(){
        return new InstantCommand(() -> {
            turretM.setPower(0);
            turretS.setPower(0);
        }).requires(this);
    }

    public Command trackTurret(){
        return new InstantCommand(
                () -> {
                    controller.setSetpoint(turretTargetAngle);
                }
        );
    }


}
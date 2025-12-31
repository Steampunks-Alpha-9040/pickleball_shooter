package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.PIDposition;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM;
    private CRServoEx turretS;

    private double turretQuad;

    private double turretTargetAngle;


    private final PIDposition controller = new PIDposition(
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

        setTurret();
        moveTurret();

        ActiveOpMode.telemetry().addData("turretTargetAngle", turretTargetAngle);
        ActiveOpMode.telemetry().addData("turretEncoder", turretQuad);
    }

    public double calculateTurretAngle(){
        double offset=0;

        switch (Constants.OpModeConstants.side){
            case RED:
                return Math.atan2(
                        Constants.OpModeConstants.BLUEscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.BLUEscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS)+offset;
            case BLUE:
                return Math.atan2(
                        Constants.OpModeConstants.REDscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH),
                        Constants.OpModeConstants.REDscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH)
                ) - Drivebase.INSTANCE.getBotpose().getHeading(AngleUnit.RADIANS)+offset;

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


    private void setTurret(){
        controller.setSetpoint(turretTargetAngle);
    }

    private void moveTurret(){
        double pow = controller.calculate(turretQuad);
        turretM.setPower(-pow);
        turretS.setPower(-pow);
    }


}
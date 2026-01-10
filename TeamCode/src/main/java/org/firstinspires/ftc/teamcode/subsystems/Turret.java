package org.firstinspires.ftc.teamcode.subsystems;

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

    private Constants.Side side;

    private boolean homed = false;


    private PIDposition controller = new PIDposition(
            Constants.TurretConstants.turret_kP,
            Constants.TurretConstants.turret_kI,
            Constants.TurretConstants.turret_kD,
            Constants.TurretConstants.turret_kF,
            Constants.TurretConstants.turretTolerance_VisionAngleRad);


    @Override
    public void initialize(){
        homed = false;
        turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
        turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);

        turretQuad = 0;
    }

    @Override
    public void periodic(){
        turretQuad = Drivebase.INSTANCE.getTurretQuadature();
        turretTargetAngle = physicalLimit();
        controller.setSetpoint(turretTargetAngle);
        if (homed) {
            controller.setSetpoint(0);
        }
        double pow = controller.calculate(turretQuad);
        if (ActiveOpMode.isStarted()) {
            turretM.setPower(pow);
            turretS.setPower(pow);
        }
        ActiveOpMode.telemetry().addData("homed: ", homed);
//        ActiveOpMode.telemetry().addData("targetAngle", turretTargetAngle);
//        ActiveOpMode.telemetry().addData("turretEncoder", turretQuad);
    }

    public void setSide(Constants.Side side){
        this.side = side;
    }


    public double physicalLimit(){
        if (calculateTurretAngle() < -Math.PI/2){
            return -Math.PI/2;
        } else return Math.min(calculateTurretAngle(), Math.PI / 2);
    }

    public Command setHomeTrue() {
        return new InstantCommand(
                () -> {
                    sethome(true);
                }
        );
    }
    public void sethome(boolean bool) {
        homed = bool;
    }

    public double calculateTurretAngle(){
        switch (side){
            case RED:
                double redVal = Math.atan2(
                        Drivebase.INSTANCE.getFollower().getPose().getY() - Constants.OpModeConstants.REDscore.getY(),
                        Constants.OpModeConstants.REDscore.getX() - Drivebase.INSTANCE.getFollower().getPose().getX()
                ) + Drivebase.INSTANCE.getFollower().getPose().getHeading();
                return Math.atan2(Math.sin(redVal), Math.cos(redVal));
            case BLUE:
                double blueVal = Math.atan2(
                        Drivebase.INSTANCE.getFollower().getPose().getY() - Constants.OpModeConstants.BLUEscore.getY(),
                        Constants.OpModeConstants.BLUEscore.getX() - Drivebase.INSTANCE.getFollower().getPose().getX()
                ) + Drivebase.INSTANCE.getFollower().getPose().getHeading();
                return Math.atan2(Math.sin(blueVal), Math.cos(blueVal));
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

    public Command moveTurretZero(){
        return new InstantCommand(() -> {
            turretTargetAngle = 0;
        });
    }

    public Command trackTurret(){
        return new InstantCommand(
                () -> {
                    controller.setSetpoint(turretTargetAngle);
                }
        );
    }


}
package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.RelativeShooting;

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

    private RelativeShooting relativeShooting = new RelativeShooting();


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
        relativeShooting.update();

        turretTargetAngle = -relativePhysicalLimit();
        controller.setSetpoint(turretTargetAngle);
        if (homed) {
            controller.setSetpoint(0);
        }
        double pow = controller.calculate(turretQuad);
        if (ActiveOpMode.isStarted()) {
            turretM.setPower(pow);
            turretS.setPower(pow);
        }
//        ActiveOpMode.telemetry().addData("homed: ", homed);
//        ActiveOpMode.telemetry().addData("RelativeTurret", relativeShooting.getTurretTarget());
        ActiveOpMode.telemetry().addData("targetAngle", turretTargetAngle);
        ActiveOpMode.telemetry().addData("turretEncoder", turretQuad);
    }

    public void setSide(Constants.Side side){
        this.side = side;
    }

    public double physicalLimit(){
        if (calculateTurretAngle() < -Math.PI/2){
            return -Math.PI/2;
        } else return Math.min(calculateTurretAngle(), Math.PI / 2);
    }

    public double relativePhysicalLimit(){
        if (calculateRelativeTurretAngle() < -Math.PI/2 + 0.05){
            return -Math.PI/2;
        } else return Math.min(calculateRelativeTurretAngle(), Math.PI / 2 - 0.05);
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
                        -Constants.OpModeConstants.REDscore.getY() + Drivebase.INSTANCE.getFollower().getPose().getY(),
                        Constants.OpModeConstants.REDscore.getX() - Drivebase.INSTANCE.getFollower().getPose().getX()
                ) + Drivebase.INSTANCE.getFollower().getPose().getHeading();
                return Math.atan2(Math.sin(redVal), Math.cos(redVal));
            case BLUE:
                double blueVal = Math.atan2(
                        -Constants.OpModeConstants.BLUEscore.getY() + Drivebase.INSTANCE.getFollower().getPose().getY(),
                        Constants.OpModeConstants.BLUEscore.getX() - Drivebase.INSTANCE.getFollower().getPose().getX()
                ) + Drivebase.INSTANCE.getFollower().getPose().getHeading();
                return Math.atan2(Math.sin(blueVal), Math.cos(blueVal));
            default:
                return 0;
        }
    }

    public double calculateRelativeTurretAngle(){
        return relativeShooting.getTurretTarget();
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


    public void setTurretQuad(double var){
        turretQuad = var;
    }

    public Command setTurretQuadCommand(double var){
        return new InstantCommand(() -> setTurretQuad(var));
    }

    public Command changePID(){
        return new InstantCommand(() -> {
            controller.setPID(
                    Constants.TurretConstants.turret_kP,
                    Constants.TurretConstants.turret_kI,
                    Constants.TurretConstants.turret_kD,
                    Constants.TurretConstants.turret_kF,
                    Constants.TurretConstants.turretTolerance_VisionAngleRad);
        });
    }


}
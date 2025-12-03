package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM;
    private CRServoEx turretS;

    private double turretQuad;

    private final ControlSystem turretPIDF = ControlSystem.builder()
            .posPid(
                    Constants.TurretConstants.turret_kP,
                    Constants.TurretConstants.turret_kI,
                    Constants.TurretConstants.turret_kD
            )
            .basicFF(
                    Constants.TurretConstants.turret_kF
            )
            .build();


    @Override
    public void initialize(){
        turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
        turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);
        turretPIDF.setGoal(new KineticState(0));
        turretQuad = 0;
    }

    @Override
    public void periodic(){


    }



    public Command spinTurretRight(){

        return new InstantCommand(() -> {
            turretM.setPower(0.1);
            turretS.setPower(0.1);
        }).requires(this);
//        return new ParallelGroup(
//                new LambdaCommand()
//                        .setStart(() -> turretM.setPower(.1))
//                        .requires(this),
//                new LambdaCommand()
//                        .setStart(() -> turretS.setPower(.1))
//                        .requires(this)
//        ).requires(this);

//        return new RunToVelocity(turretPIDF, )
    }
    public Command spinTurretLeft(){
        return new InstantCommand(() -> {
            turretM.setPower(-0.1);
            turretS.setPower(-0.1);
        }).requires(this);
//        return new ParallelGroup(
//                new LambdaCommand()
//                        .setStart(() -> turretM.setPower(-.1))
//                        .requires(this),
//                new LambdaCommand()
//                        .setStart(() -> turretS.setPower(-.1))
//                        .requires(this)
//        ).requires(this);


    }
    public Command stopTurret(){
//        return new ParallelGroup(
//                new LambdaCommand()
//                        .setStart(() -> turretM.setPower(0))
//                        .requires(this),
//                new LambdaCommand()
//                        .setStart(() -> turretS.setPower(0))
//                        .requires(this)
//        ).requires(this);
//        return new LambdaCommand()
//                .setStart(() -> {
//                    turretM.setPower(0.0);
//                    turretS.setPower(0.0);
//                })
//                .requires(this);
        return new InstantCommand(() -> {
            turretM.setPower(0);
            turretS.setPower(0);
        }).requires(this);
    }

    public Command trackTurret(){
        return new RunToPosition(turretPIDF, 0, Constants.TurretConstants.turretTolerance_VisionAngleDeg)
                .then(stopTurret())
            .requires(this)
                .setInterruptible(true);

    }

//    public void setTurretDirection(Vision vision){
//        if (vision.getTurretDirection() == Vision.TurretDirection.LEFT) {
//            turretM.setPower(-0.8);
//            turretS.setPower(0.8);
//        } else if (vision.getTurretDirection() == Vision.TurretDirection.RIGHT){
//            turretM.setPower(0.8);
//            turretS.setPower(-0.8);
//        } else if (vision.getTurretDirection() == Vision.TurretDirection.STOP){
//            turretM.setPower(0.0);
//            turretS.setPower(0.0);
//        }
//    }
}
package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.PerpetualCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
    private CRServoEx turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);


    private final ControlSystem turretPIDF = ControlSystem.builder()
            .velPid(
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

    }

    @Override
    public void periodic(){
    }



    public Command spinTurretRight(){
        return new LambdaCommand()
                .setStart(() -> {
                    turretM.setPower(0.1);
                    turretS.setPower(.1);
                })
                .requires(this);
    }
    public Command spinTurretLeft(){
        return new LambdaCommand()
                .setStart(() -> {
                    turretM.setPower(-.1);
                    turretS.setPower(-.1);
                })
                .requires(this);
    }
    public Command stopTurret(){
        return new LambdaCommand()
                .setStart(() -> {
                    turretM.setPower(0.0);
                    turretS.setPower(0.0);
                })
                .requires(this);
    }

    public Command trackTurret(){
        return new LambdaCommand()
                .requires(this)
                        .setUpdate(() -> {
                            turretS.setPower(Util.clamp(Vision.INSTANCE.getHorizontalTy()+0.05/80, -0.1, 0.1));
                            turretM.setPower(Util.clamp(Vision.INSTANCE.getHorizontalTy()+0.05/80, -0.1, 0.1));
                        }
        );
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
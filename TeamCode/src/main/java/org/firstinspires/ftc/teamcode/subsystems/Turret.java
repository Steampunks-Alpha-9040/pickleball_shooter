package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.PerpetualCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;

public class Turret implements Subsystem {
    public static final Turret INSTANCE = new Turret();

    private CRServoEx turretM = new CRServoEx(Constants.TurretConstants.turretMasterName);
    private CRServoEx turretS = new CRServoEx(Constants.TurretConstants.turretSlaveName);

    @Override
    public void initialize(){

    }

    public Command spinTurretRight(){
        return new LambdaCommand()
                .setStart(() -> {
                    turretM.setPower(0.8);
                    turretS.setPower(-0.8);
                })
                .requires(this);
    }
    public Command spinTurretLeft(){
        return new LambdaCommand()
                .setStart(() -> {
                    turretM.setPower(-0.8);
                    turretS.setPower(0.8);
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

    public Command trackTurret(Vision vision){
        return new LambdaCommand()
                .requires(this)
                .setUpdate(() -> {
                    setTurretDirection(vision);
                })
                .setInterruptible(true);
    }

    public  setTurretDirection(Vision vision){
        switch (vision.getTurretDirection()){
            case LEFT -> {

            }
        }
    }
}
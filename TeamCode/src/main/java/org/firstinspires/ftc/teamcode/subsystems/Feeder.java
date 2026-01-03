package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;


public class Feeder implements Subsystem {

    public static Feeder INSTANCE = new Feeder();

    private CRServoEx feederM;
    private CRServoEx feederS;
    private ServoEx feederArm;



    @Override
    public void initialize(){
        feederM = new CRServoEx(Constants.FeederConstants.feederM);
        feederS = new CRServoEx(Constants.FeederConstants.feederS);
        feederArm = new ServoEx(Constants.FeederConstants.feederArm);
    }

    @Override
    public void periodic(){


    }

    public Command transfer(){
        return new ParallelGroup(
                setArmDown(),
                turnWheelsOn()
        );
    }

    public Command store(){
        return new ParallelGroup(
                setArmUp(),
                turnWheelsOff()
        );
    }

    public Command setArmDown(){
        return new LambdaCommand()
                .setStart(() -> feederArm.setPosition(0.9));
    }

    public Command setArmUp(){
        return new LambdaCommand()
                .setStart(() -> feederArm.setPosition(0.0));
    }

    public Command turnWheelsOn(){
        return new InstantCommand(
                () -> {
                    feederM.setPower(-1);
                    feederS.setPower(1);
                }
        );
    }

    public Command turnWheelsOff(){
        return new InstantCommand(
                () -> {
                    feederM.setPower(0);
                    feederS.setPower(0);
                }
        );
    }

}

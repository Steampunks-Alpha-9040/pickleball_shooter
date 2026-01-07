package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;


public class Feeder implements Subsystem {

    public static Feeder INSTANCE = new Feeder();

    private CRServoEx feeder1;
    private CRServoEx feeder2;
    private ServoEx feederArm;



    @Override
    public void initialize(){
        feeder1 = new CRServoEx(Constants.FeederConstants.feederWheel1);
        feeder2 = new CRServoEx(Constants.FeederConstants.feederWheel2);
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
                .requires(this)
                .setStart(() -> feederArm.setPosition(0.9));
    }

    public Command setArmUp(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> feederArm.setPosition(0.0));
    }

    public Command turnWheelsOn(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> {
                    feeder1.setPower(1);
                    feeder2.setPower(-1);
                });
    }

    public Command turnWheelsOff(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> {
                    feeder1.setPower(0);
                    feeder2.setPower(0);
                });
    }

}

package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;


public class Feeder implements Subsystem {

    public static Feeder INSTANCE = new Feeder();

    private MotorEx feederWheel;
    private ServoEx feederArm;



    @Override
    public void initialize(){
        feederWheel = new MotorEx(Constants.FeederConstants.feederWheel).brakeMode().reversed();
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
                .setStart(() -> feederArm.setPosition(0.75));
    }

    public Command setArmUp(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> feederArm.setPosition(0));
    }

    public Command turnWheelsOn(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> feederWheel.setPower(1));
    }

    public Command turnWheelsOff(){
        return new LambdaCommand()
                .requires(this)
                .setStart(() -> feederWheel.setPower(0));
    }

}

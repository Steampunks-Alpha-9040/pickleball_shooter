package org.firstinspires.ftc.teamcode.subsystems;


import android.graphics.Color;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Utilities.ContinuousInputPID;
import org.firstinspires.ftc.teamcode.util.PIDposition;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDElement;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;

public class Indexer implements Subsystem {

    public static final Indexer INSTANCE = new Indexer();

    private CRServoEx indexer1;
    private CRServoEx indexer2;
    private AnalogInput indexerEncoder;
    private ContinuousInputPID contPID = new ContinuousInputPID(
            Constants.IndexerConstants.indexer_kP,
            Constants.IndexerConstants.indexer_kI,
            Constants.IndexerConstants.indexer_kD,
            Constants.IndexerConstants.indexer_kF
    );
    ColorSensor color1;
    ColorSensor color2;
    ColorSensor color3;
    public int pattern = 0;

    boolean stopPID = false;
    public void setPattern(int id) {
        pattern = id;
    }


    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER, NONE,
    }



    @Override
    public void initialize(){
        indexer1 = new CRServoEx(Constants.IndexerConstants.indexer1);
        indexer1 = new CRServoEx(Constants.IndexerConstants.indexer2);
        indexerEncoder = ActiveOpMode.hardwareMap().analogInput.get("indexerEncoder");
        contPID.enableContinuousInput(-Math.PI, Math.PI);
    }

    public Command setPos(double rad) {
        return new InstantCommand(() -> contPID.setSetPoint(rad));
    }

    public double getPos() {
        double rawVoltage = indexerEncoder.getVoltage();
        double angle = ((3.3 - rawVoltage) / 3.3 * 2 * Math.PI - Math.PI);
        return angle;
    }

    public Command moveToDetect() {
        if (contPID.error(getPos(),0) < contPID.error(getPos(), 2*Math.PI/3)
        && contPID.error(getPos(), 0) < contPID.error(getPos(), -2*Math.PI/3)){
            return new InstantCommand(() -> contPID.setSetPoint(0));
        }
        if (contPID.error(getPos(),2*Math.PI/3) < contPID.error(getPos(), 0)
                && contPID.error(getPos(), 2*Math.PI/3) < contPID.error(getPos(), -2*Math.PI/3)){
            return new InstantCommand(() -> contPID.setSetPoint(2*Math.PI/3));
        }if (contPID.error(getPos(),-2*Math.PI/3) < contPID.error(getPos(), 2*Math.PI/3)
                && contPID.error(getPos(), -2*Math.PI/3) < contPID.error(getPos(), 0)){
            return new InstantCommand(() -> contPID.setSetPoint(-2*Math.PI/3));
        } else {
            return new InstantCommand(() -> contPID.setSetPoint(0));
        }

    }

    public Command detectColor() {
        return new SequentialGroup(

        )
    }


    public Command spinIndexerSlow(){
        return new InstantCommand(() ->
        {indexer1.setPower(0.5);
        indexer2.setPower(0.5);
        });
    }
    public Command spinIndexerFast(){
        return new InstantCommand(() ->
        {indexer1.setPower(0.8);
            indexer2.setPower(0.8);
        });
    }
    public Command stopIndexer(){
        return new InstantCommand(() ->
        {indexer1.setPower(0.0);
            indexer2.setPower(0.0);
        });
    }





}

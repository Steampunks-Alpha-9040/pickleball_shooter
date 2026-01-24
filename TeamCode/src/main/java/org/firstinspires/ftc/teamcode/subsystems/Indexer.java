package org.firstinspires.ftc.teamcode.subsystems;


import android.graphics.Color;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.ContinuousInputsPID;
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

    private enum GreenPos{
        LAST, MIDDLE, FIRST, NONE
    }
    private CRServoEx indexer1;
    private CRServoEx indexer2;
    private MotorEx indexer;
    private AnalogInput indexerEncoder;
    private final ContinuousInputsPID contPID = new ContinuousInputsPID(
            Constants.indexer_kP,
            Constants.indexer_kI,
            Constants.indexer_kD,
            Constants.indexer_kF
    );
    ColorSensor color1;
    ColorSensor color2;
    ColorSensor color3;

    public int greenLocation;
    public int pattern = 0;

    private double power = 0.0;

    boolean stopPID = false;
    public void setPattern(int id) {
        pattern = id;
    }


    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER, NONE,
    }



    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer1);
//        indexer1 = new CRServoEx(Constants.IndexerConstants.indexer1);
//        indexer2 = new CRServoEx(Constants.IndexerConstants.indexer2);
        indexerEncoder = ActiveOpMode.hardwareMap().get(AnalogInput.class, Constants.IndexerConstants.indexerEncoder);
    }

    @Override
    public void periodic() {

        ActiveOpMode.telemetry().addData("index Encoder", getPos());

//        double power = contPID.calculate(getPos());
//        if (!stopPID) {
//            indexer1.setPower(power);
//            indexer2.setPower(power);
//        }
//        indexer1.setPower(1);
//        indexer2.setPower(1);

        indexer.setPower(power);

//        indexer1.setPower(power);
//        indexer2.setPower(power);

        ActiveOpMode.telemetry().addData("Power: ", power);


//        ActiveOpMode.telemetry().addData("R", color1.red());
//        ActiveOpMode.telemetry().addData("B", color1.blue());
//        ActiveOpMode.telemetry().addData("G", color1.green());

    }

    public double getPos() {
        double rawVoltage = indexerEncoder.getVoltage();
        double offset = 0.7;
        return (((rawVoltage / 3.3)*2*Math.PI) - Math.PI) - offset;
    }

    public Command spinIndexerThird() {
        return new InstantCommand(() -> contPID.setSetPoint((2*Math.PI)/3));
    }

    public Command spinIndexerSecond() {
        return new InstantCommand(() -> contPID.setSetPoint(-(2*Math.PI)/3));
    }
    public Command spinIndexerMiddle(){
        return new InstantCommand(() -> contPID.setSetPoint(0));
    }

//    public Command spinGreenCorrect(){
//        switch (Vision.INSTANCE.getMatchPattern()){
//            case PGP:
//                if (getGreenPos() == GreenPos.FIRST){
//                    return spinIndexerSecond();
//                } else if (getGreenPos() == GreenPos.MIDDLE){
//                    return stopIndexer();
//                } else {
//                    return spinIndexerThird();
//                }
//            case PPG:
//                if (getGreenPos() == GreenPos.FIRST){
//                    return spinIndexerThird();
//                } else if (getGreenPos() == GreenPos.MIDDLE){
//                    return spinIndexerSecond();
//                } else {
//                    return stopIndexer();
//                }
//            case GPP:
//                if (getGreenPos() == GreenPos.FIRST){
//                    return stopIndexer();
//                } else if (getGreenPos() == GreenPos.MIDDLE){
//                    return spinIndexerThird();
//                } else {
//                    return spinIndexerSecond();
//                }
//            default:
//                return stopIndexer();
//        }
//    }

//    public GreenPos getGreenPos(){
//        if (color1.green() > 800){
//            return GreenPos.FIRST;
//        } else if (color2.green() > 800){
//            return GreenPos.MIDDLE;
//        } else if (color3.green() > 800){
//            return GreenPos.LAST;
//        } else {
//            return GreenPos.NONE;
//        }
//
//    }




    public Command spinIndexerSlow(){
        return new InstantCommand(() -> {
            power = 0.4;
        });
    }
    public Command spinIndexerFast(){
        return new InstantCommand(() -> {
            power = 0.5;
        });
    }
    public Command stopIndexer(){
        return new InstantCommand(() -> {
            power = 0.0;
        });
    }

    public Command changePID(){
        return new InstantCommand(() -> {
            contPID.setPID(
                    Constants.indexer_kP,
                    Constants.indexer_kI,
                    Constants.indexer_kD,
                    Constants.indexer_kF
                    );
        });
    }



}

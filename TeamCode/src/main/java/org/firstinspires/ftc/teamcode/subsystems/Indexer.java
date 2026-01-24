package org.firstinspires.ftc.teamcode.subsystems;


import android.graphics.Color;

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

    private CRServoEx indexer1;
    private CRServoEx indexer2;
    private AnalogInput indexerEncoder;
    private ContinuousInputsPID contPID = new ContinuousInputsPID(
            Constants.indexer_kP,
            Constants.indexer_kI,
            Constants.indexer_kD,
            Constants.indexer_kF
    );
//    ColorSensor color1;
//    ColorSensor color2;
//    ColorSensor color3;

    private final double offset = 0.7;
    public int greenLocation;
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
        indexer2 = new CRServoEx(Constants.IndexerConstants.indexer2);
        indexerEncoder = ActiveOpMode.hardwareMap().get(AnalogInput.class, Constants.IndexerConstants.indexerEncoder);
    }

    @Override
    public void periodic() {

        ActiveOpMode.telemetry().addData("index Encoder", getPos());

        double power = contPID.calculate(getPos());
        if (!stopPID) {
            indexer1.setPower(power);
            indexer2.setPower(power);
        }
//        indexer1.setPower(1);
//        indexer2.setPower(1);

        ActiveOpMode.telemetry().addData("Power: ", power);


//        ActiveOpMode.telemetry().addData("R", color1.red());
//        ActiveOpMode.telemetry().addData("B", color1.blue());
//        ActiveOpMode.telemetry().addData("G", color1.green());



    }

    public double getPos() {
        double rawVoltage = indexerEncoder.getVoltage();
        return (((rawVoltage / 3.3)*2*Math.PI) - Math.PI) - offset;
    }

    public Command spinIndexerThird(){
        return new InstantCommand(() -> contPID.setSetPoint((2*Math.PI)/3));
    }

    public Command spinIndexerSecond(){
        return new InstantCommand(() -> contPID.setSetPoint((Math.PI)/3));
    }



    public Command spinIndexerSlow(){
        return new InstantCommand(() -> {
            indexer1.setPower(0.4);
            indexer2.setPower(0.4);
        });
    }
    public Command spinIndexerFast(){
        return new InstantCommand(() -> {
            indexer1.setPower(0.5);
            indexer2.setPower(0.5);
        });
    }
    public Command stopIndexer(){
        return new InstantCommand(() -> {
            indexer1.setPower(0.0);
            indexer2.setPower(0.0);
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

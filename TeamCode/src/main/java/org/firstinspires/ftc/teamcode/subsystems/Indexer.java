package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDElement;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Indexer implements Subsystem {

    public static final Indexer INSTANCE = new Indexer();

    private MotorEx indexer;

    private final ControlSystem indexerCalculator = ControlSystem.builder()
            .velPid(
                    Constants.IndexerConstants.indexer_kP,
                    Constants.IndexerConstants.indexer_kI,
                    Constants.IndexerConstants.indexer_kD
            )
            .basicFF(
                    Constants.IndexerConstants.indexer_kF
            )
            .build();

    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER
    }


    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer);
    }


    public Command spinIndexer(){
        return new LambdaCommand("spinIndexer")
                .setStart(() -> spinIndexer(Constants.IndexerConstants.indexer_DutyCycle))
                .addRequirements(this);
    }

    public Command stopIndexer(){
        return new LambdaCommand("spinIndexer")
                .setStart(() -> spinIndexer(0.0))
                .addRequirements(this);
    }


    public Command setIndexerPos(IndexerState greenPos){
        switch(greenPos){
            case RIGHT:
                return new LambdaCommand("setIndexerPos")
                        .setStart(() -> setIndexerPosition(0.5))
                        .addRequirements(this);
            case LEFT:
                return new LambdaCommand("setIndexerPos")
                        .setStart(() -> setIndexerPosition(0.2))
                        .addRequirements(this);
            case CENTER:
                return new LambdaCommand("setIndexerPos")
                        .setStart(() -> setIndexerPosition(-0.2))
                        .addRequirements(this);
            default:
                return new LambdaCommand("EMPTY");
        }
    }

    private void spinIndexer(double power){
        INSTANCE.indexer.setPower(power);
    }

    private void setIndexerPosition(double pos){

    }

}

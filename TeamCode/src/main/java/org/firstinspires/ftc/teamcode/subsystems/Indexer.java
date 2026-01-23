package org.firstinspires.ftc.teamcode.subsystems;


import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
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
import dev.nextftc.hardware.impl.MotorEx;

public class Indexer implements Subsystem {

    public static final Indexer INSTANCE = new Indexer();
    private MotorEx indexer;
//    ColorSensor color1;
//    ColorSensor color2;
//    ColorSensor color3;
    private final double TicksPerRot =  (Util.GoBILDA.RPM_1620.getCPR()) * (340.0/80.0);
    public int pattern = 0;
    boolean stopPID = false;
    double power = 0;
    double target=0;
    public double testing = 0.0;
    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER
    }

    private final PIDposition indexerCalculator = new PIDposition(
            Constants.IndexerConstants.indexer_kP,
            Constants.IndexerConstants.indexer_kI,
            Constants.IndexerConstants.indexer_kD,
            Constants.IndexerConstants.indexer_kF,
            Constants.IndexerConstants.indexerTolerance)
            ;




    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer).zeroed();
    }

    @Override
    public void periodic(){

        indexer.setPower(indexerCalculator.calculate(Drivebase.INSTANCE.getHoodQuadature()));
        ActiveOpMode.telemetry().addData("testingVal: ", testing);
    }

    public void setPattern(int id) {
        pattern = id;
    }

    public Command spinIndexer(double power){
        return new InstantCommand(() -> indexer.setPower(power));
    }

    public Command changeTestingCommand(double delta){
        return new InstantCommand(() -> changeTesting(delta));
    }

    public void changeTesting(double delta){
        testing += delta;
    }

    public Command spinIndexerSlow(){
        return new InstantCommand(() -> indexer.setPower(0.4));
    }
    public Command spinIndexerFast(){
        return new InstantCommand(() -> indexer.setPower(0.5));
    }
    public Command stopIndexer(){
        return new InstantCommand(() -> indexer.setPower(0.0));
    }



}

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
    public void setPattern(int id) {
        pattern = id;
    }

    double power = 0;
    double target=0;


    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER
    }



    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer).zeroed();
    }


    public Command spinIndexer(){
        return new InstantCommand(() -> indexer.setPower(0.2));
    }
    public Command stopIndexer(){
        return new InstantCommand(() -> indexer.setPower(0.0));
    }



}

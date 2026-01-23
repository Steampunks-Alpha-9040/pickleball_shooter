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
    ColorSensor color1;
    ColorSensor color2;
    ColorSensor color3;
    private final double TicksPerRot =  (Util.GoBILDA.RPM_312.getCPR()) * (340.0/80.0);
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
            Constants.IndexerConstants.indexerTolerance
    );




    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer).zeroed();
        indexer.setCurrentPosition(0);
    }

    @Override
    public void periodic(){
        indexerCalculator.setSetpoint(testing);
        double power = indexerCalculator.calculate(indexer.getCurrentPosition());
        if(power > 0.7){
            power = 0.7;
        } else if (power < -0.7){
            power = -0.7;
        }
        indexer.setPower(power);
        ActiveOpMode.telemetry().addData("testingVal: ", testing);
        ActiveOpMode.telemetry().addData("currentPose: ", indexer.getCurrentPosition());

    }

    public void setPattern(int id) {
        pattern = id;
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

    public Command changePID(){
        return new InstantCommand(() -> {
            indexerCalculator.setPID(
                    Constants.IndexerConstants.indexer_kP,
                    Constants.IndexerConstants.indexer_kI,
                    Constants.IndexerConstants.indexer_kD,
                    Constants.IndexerConstants.indexer_kF,
                    Constants.IndexerConstants.indexerTolerance);
        });
    }

    int ticksCenter = (int) (TicksPerRot/3 + pattern*TicksPerRot);
    int ticksRight = (int) (2*TicksPerRot/3 + pattern*TicksPerRot);
    int ticksLeft = (int) (pattern*TicksPerRot);


    int currticks = 0;
    IndexerState greenState = null;
    public Command autoSet() {
        return new SequentialGroup(
                movetocheckColor(),
                sort()).setInterruptible(true);
    }

    public boolean checkValid() {
        if (greenState == IndexerState.LEFT) {
            return checkPos(ticksLeft,50);
        }
        if (greenState == IndexerState.RIGHT) {
            return checkPos(ticksRight, 50);
        }
        if (greenState == IndexerState.CENTER) {
            return checkPos(ticksCenter, 50);
        }
        return true;
    }


    public Command setPos(IndexerState state) {

        if (state == IndexerState.CENTER) {
            target = ticksCenter;
        } else if (state == IndexerState.RIGHT) {
            target = ticksRight;
        } else if (state == IndexerState.LEFT) {
            target = ticksLeft;
        }
        currticks = (int) indexer.getRawTicks();

        if (Math.abs(target-(Math.floorMod(currticks, (int)TicksPerRot))) < (double) TicksPerRot/2) {
            target = (int) ((currticks/TicksPerRot) * TicksPerRot + (target));
        } else {
            target = (int) ((currticks/TicksPerRot) * TicksPerRot - (target));
        }
        return new InstantCommand(() -> indexerCalculator.setSetpoint(target));
    }

    public Command movetocheckColor() {
        target = 0; //settocheckColor
        currticks = (int) indexer.getRawTicks();

        if (Math.abs(target-(Math.floorMod(currticks, (int) TicksPerRot/3))) < (double) (TicksPerRot/3)/2) {
            target = (int) ((currticks/(TicksPerRot/3)) * (TicksPerRot/3) + (target));
        } else {
            target = (int) ((currticks/(TicksPerRot/3)) * (TicksPerRot/3) - (target));
        }
        return new InstantCommand(() -> indexerCalculator.setSetpoint((double) target)).requires(this);
    }
    public Command sort() {
        if (greenState != null) {
            greenState = checkColor();
        }
        return setPos(greenState);
    }
    public IndexerState checkColor() {

        if (checkPos((int) (TicksPerRot/6), 50)) {
            if (color1.green() > 100) {
                return IndexerState.CENTER;
            }
            if (color2.green() > 100) {
                return IndexerState.RIGHT;
            }
            if (color3.green() > 100) {
                return IndexerState.LEFT;
            }
        } else if (checkPos((int) (TicksPerRot/2), 50)) {
            if (color1.green() > 100) {
                return IndexerState.RIGHT;
            }
            if (color2.green() > 100) {
                return IndexerState.LEFT;
            }
            if (color3.green() > 100) {
                return IndexerState.CENTER;
            }
        } else if (checkPos((int) (5*TicksPerRot/6), 50)) {
            if (color1.green() > 100) {
                return IndexerState.LEFT;
            }
            if (color2.green() > 100) {
                return IndexerState.CENTER;
            }
            if (color3.green() > 100) {
                return IndexerState.RIGHT;
            }
        } else {
            return null;
        }
        return null;
    }

    public boolean checkPos(int ticks, int tolerance) {
        currticks = (int) indexer.getRawTicks();
        if (Math.abs(ticks-currticks)%TicksPerRot < tolerance) {
            return true;
        } else {
            return false;
        }
    }

    public Command oneRot() {
        return new InstantCommand(() -> indexerCalculator.setSetpoint(indexer.getCurrentPosition()+TicksPerRot));
    }

    public void spinIndexer(double power){
        INSTANCE.indexer.setPower(power);
    }




}

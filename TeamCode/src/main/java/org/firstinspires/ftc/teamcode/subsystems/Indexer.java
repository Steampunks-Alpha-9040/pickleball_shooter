package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Utilities.ContinuousInputPID;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;

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
    public int greenLocation;
    public int pattern = 0;

    boolean stopPID = true;
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
        indexerEncoder = ActiveOpMode.hardwareMap().analogInput.get("indexerEncoder");
        contPID.enableContinuousInput(-Math.PI, Math.PI);
        contPID.setTolerance(0.05);
        color1 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color1");
        color2 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color2");
        color3 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color3");
    }

    @Override
    public void periodic() {
        double power = contPID.calculate(getPos());
        if (!stopPID) {
            indexer1.setPower(power);
            indexer2.setPower(power);
        }


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

    public boolean ready() {
        if (Math.abs(contPID.getPositionError()) < contPID.getTolerance()[0] + 0.02) {
            return true;
        } else {
            return false;
        }
    }

    public void setStopPID(boolean stopPID) {
        this.stopPID = stopPID;
    }

    public Command startPID() {
        return new InstantCommand(() -> setStopPID(false));
    }

    public Command stopPID() {
        return new InstantCommand(() -> setStopPID(true));
    }

    public Command detectCommand() {
        return new InstantCommand(() -> detect());
    }
    public void detect() {
        greenLocation = detectHelper();
    }

    public int detectHelper() {
        if (Math.abs(contPID.getSetPoint()) < 1e-3) {
            if (color1.green() > 100) {
                return 1;
            }
            if (color2.green() > 100) {
                return 2;
            }
            if (color3.green() > 100) {
                return 3;
            }
        } else if (Math.abs(contPID.getSetPoint()) - 2*Math.PI/3 < 1e-3) {
            if (color1.green() > 100) {
                return 2;
            }
            if (color2.green() > 100) {
                return 3;
            }
            if (color3.green() > 100) {
                return 1;
            }
        } else if (Math.abs(contPID.getSetPoint()) + 2*Math.PI/3 < 1e-3) {
            if (color1.green() > 100) {
                return 3;
            }
            if (color2.green() > 100) {
                return 1;
            }
            if (color3.green() > 100) {
                return 2;
            }
        } else {
            return 0;
        }
        return 0;
    }

    public Command detectColor() {
        return new SequentialGroup(moveToDetect(), new WaitUntil(() -> ready()), detectCommand());
    }

    public Command sort() {
        return new SequentialGroup(startPID(), detectColor(), moveToSort());
    }

    public Command moveToSort() {
        double target;
        if (greenLocation == 0) {
            return new NullCommand();
        }
        if (greenLocation == 1) {
            return setPos(Math.PI/3);
        }
        if (greenLocation == 2) {
            return setPos(Math.PI);
        }
        if (greenLocation == 3) {
            return setPos(-Math.PI/3);
        } else {
            return new NullCommand();
        }
    }


    public Command spinIndexerSlow(){
        return new InstantCommand(() ->
        {stopPID();
            indexer1.setPower(0.5);
        indexer2.setPower(0.5);
        });
    }
    public Command spinIndexerFast(){
        return new InstantCommand(() ->
        {stopPID();
            indexer1.setPower(0.8);
            indexer2.setPower(0.8);
        });
    }
    public Command stopIndexer(){
        return new InstantCommand(() ->
        {stopPID();
            indexer1.setPower(0.0);
            indexer2.setPower(0.0);
        });
    }





}

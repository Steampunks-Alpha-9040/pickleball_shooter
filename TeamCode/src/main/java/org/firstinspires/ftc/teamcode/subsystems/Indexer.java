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

    @Override
    public void periodic() {
        if (stopPID == false) {
            double power = -indexerCalculator.calculate(indexer.getCurrentPosition());
        }
        indexer.setPower(-power);
//        ActiveOpMode.telemetry().addData("Spindex Setpoint:", indexerCalculator.getSetpoint());
//        ActiveOpMode.telemetry().addData("Power Spindex:", power);
//        ActiveOpMode.telemetry().addData("Spindex Encoder:", indexer.getCurrentPosition());
    }


    private final PIDposition indexerCalculator = new PIDposition(Constants.IndexerConstants.indexer_kP,0,Constants.IndexerConstants.indexer_kD,Constants.IndexerConstants.indexer_kF,50, 0.4);
    private final ControlSystem maxSpeed =
            ControlSystem.builder()

                    .posPid(1,0,0.02)
//                    .stateSupplier(() ->
//                            new KineticState(
//                                    motor.getCurrentPosition() / TICKS_PER_SLOT
//                            )
//                    )
//                    .outputConsumer(output ->
//                            motor.setPower(output)
//                    )
                    .build();

    public enum IndexerState{ // this is based off of where the green ball is
        RIGHT, LEFT, CENTER
    }


    @Override
    public void initialize(){
        indexer = new MotorEx(Constants.IndexerConstants.indexer).zeroed();
        indexer.setCurrentPosition(0);
        indexerCalculator.setSetpoint(0);
//        color1 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color1");
//        color2 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color2");
//        color3 = ActiveOpMode.hardwareMap().get(ColorSensor.class, "color3");
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

    int ticksCenter = (int) (TicksPerRot/3 + pattern*TicksPerRot);
    int ticksRight = (int) (2*TicksPerRot/3 + pattern*TicksPerRot);
    int ticksLeft = (int) (pattern*TicksPerRot);

    int target = 0;
    int currticks = 0;
    IndexerState greenState = null;
//    public Command autoSet() {
//        return new SequentialGroup(
//                movetocheckColor(),
//                sort()).setInterruptible(true);
//    }
//
//    public boolean checkValid() {
//        if (greenState == IndexerState.LEFT) {
//            return checkPos(ticksLeft,50);
//        }
//        if (greenState == IndexerState.RIGHT) {
//            return checkPos(ticksRight, 50);
//        }
//        if (greenState == IndexerState.CENTER) {
//            return checkPos(ticksCenter, 50);
//        }
//        return true;
//    }
//
//
//    public Command setPos(IndexerState state) {
//
//        if (state == IndexerState.CENTER) {
//            target = ticksCenter;
//        } else if (state == IndexerState.RIGHT) {
//            target = ticksRight;
//        } else if (state == IndexerState.LEFT) {
//            target = ticksLeft;
//        }
//        currticks = (int) indexer.getRawTicks();
//
//        if (Math.abs(target-(Math.floorMod(currticks, (int)TicksPerRot))) < (double) TicksPerRot/2) {
//            target = (int) ((currticks/TicksPerRot) * TicksPerRot + (target));
//        } else {
//            target = (int) ((currticks/TicksPerRot) * TicksPerRot - (target));
//        }
//        return new InstantCommand(() -> indexerCalculator.setSetpoint(target));
//    }
//
//    public Command movetocheckColor() {
//        target = 0; //settocheckColor
//        currticks = (int) indexer.getRawTicks();
//
//        if (Math.abs(target-(Math.floorMod(currticks, (int) TicksPerRot/3))) < (double) (TicksPerRot/3)/2) {
//            target = (int) ((currticks/(TicksPerRot/3)) * (TicksPerRot/3) + (target));
//        } else {
//            target = (int) ((currticks/(TicksPerRot/3)) * (TicksPerRot/3) - (target));
//        }
//        return new InstantCommand(() -> indexerCalculator.setSetpoint((double) target)).requires(this);
//    }
//    public Command sort() {
//        if (greenState != null) {
//            greenState = checkColor();
//        }
//        return setPos(greenState);
//    }
//    public IndexerState checkColor() {
//
//        if (checkPos((int) (TicksPerRot/6), 50)) {
//            if (color1.green() > 100) {
//                return IndexerState.CENTER;
//            }
//            if (color2.green() > 100) {
//                return IndexerState.RIGHT;
//            }
//            if (color3.green() > 100) {
//                return IndexerState.LEFT;
//            }
//        } else if (checkPos((int) (TicksPerRot/2), 50)) {
//            if (color1.green() > 100) {
//                return IndexerState.RIGHT;
//            }
//            if (color2.green() > 100) {
//                return IndexerState.LEFT;
//            }
//            if (color3.green() > 100) {
//                return IndexerState.CENTER;
//            }
//        } else if (checkPos((int) (5*TicksPerRot/6), 50)) {
//            if (color1.green() > 100) {
//                return IndexerState.LEFT;
//            }
//            if (color2.green() > 100) {
//                return IndexerState.CENTER;
//            }
//            if (color3.green() > 100) {
//                return IndexerState.RIGHT;
//            }
//        } else {
//            return null;
//        }
//        return null;
//    }
//
//    public boolean checkPos(int ticks, int tolerance) {
//        currticks = (int) indexer.getRawTicks();
//        if (Math.abs(ticks-currticks)%TicksPerRot < tolerance) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
    public Command oneRot() {
        stopPID = false;
        return new InstantCommand(() -> indexerCalculator.setSetpoint(indexer.getCurrentPosition()+TicksPerRot));
    }

    public Command shootThree() {
        stopPID = false;
        return oneRot();
    }

    public Command shootOne() {
        stopPID = false;
        return new InstantCommand(() -> indexerCalculator.setSetpoint(indexer.getCurrentPosition()+ TicksPerRot/3));
    }

    public Command shootTwo() {
        stopPID = false;
        return new InstantCommand(() -> indexerCalculator.setSetpoint(indexer.getCurrentPosition()+ 2* TicksPerRot/3));
    }

    public void spinIndexer(double power){
        stopPID = true;
        INSTANCE.indexer.setPower(power);
    }



}

package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.feedback.PIDElement;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.impl.MotorEx;

public class Indexer implements Subsystem {

    public static final Indexer INSTANCE = new Indexer();

    private MotorEx indexer;
    private ColorSensor color1;
    private ColorSensor color2;
    private ColorSensor color3;
    private int TicksPerRot = 4000; //change later

    private final ControlSystem indexerCalculator =
            ControlSystem.builder()

                    .posPid(Constants.IndexerConstants.indexer_kP,Constants.IndexerConstants.indexer_kI,Constants.IndexerConstants.indexer_kD)
                    .basicFF(
                            Constants.IndexerConstants.indexer_kF
                    )
//                    .stateSupplier(() ->
//                            new KineticState(
//                                    motor.getCurrentPosition() / TICKS_PER_SLOT
//                            )
//                    )
//                    .outputConsumer(output ->
//                            motor.setPower(output)
//                    )
                    .build();

    private final ControlSystem maxSpeed =
            ControlSystem.builder()

                    .posPid(1,0,0.01)
                    .basicFF(

                    )
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
        indexer = new MotorEx(Constants.IndexerConstants.indexer);
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        color2 = hardwareMap.get(ColorSensor.class, "color2");
        color3 = hardwareMap.get(ColorSensor.class, "color3");
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

    int ticksCenter = 0;
    int ticksRight = 0;
    int ticksLeft = 0;

    int target = 0;
    int currticks = 0;
    IndexerState greenState = null;
    public Command autoSet() {
        return new SequentialGroup(
                movetocheckColor(),
                sort());
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

        if (Math.abs(target-(currticks%TicksPerRot)) < (double) TicksPerRot/2) {
            target = (currticks/TicksPerRot) * TicksPerRot + (target);
        } else {
            target = (currticks/TicksPerRot) * TicksPerRot - (target);
        }
        return new RunToPosition(indexerCalculator, target).requires(this);
    }

    public Command movetocheckColor() {
        target = 0; //settocheckColor
        currticks = (int) indexer.getRawTicks();

        if (Math.abs(target-(currticks%(TicksPerRot/3))) < (double) (TicksPerRot/3)/2) {
            target = (currticks/(TicksPerRot/3)) * (TicksPerRot/3) + (target);
        } else {
            target = (currticks/(TicksPerRot/3)) * (TicksPerRot/3) - (target);
        }
        return new RunToPosition(indexerCalculator, target).requires(this);
    }
    public Command sort() {
        if (greenState != null) {
            greenState = checkColor();
        }
        return setPos(greenState);
    }
    public IndexerState checkColor() {

        if (checkPos(TicksPerRot/6, 50)) {
            if (color1.green() > 100) {
                return IndexerState.CENTER;
            }
            if (color2.green() > 100) {
                return IndexerState.RIGHT;
            }
            if (color3.green() > 100) {
                return IndexerState.LEFT;
            }
        } else if (checkPos(TicksPerRot/2, 50)) {
            if (color1.green() > 100) {
                return IndexerState.RIGHT;
            }
            if (color2.green() > 100) {
                return IndexerState.LEFT;
            }
            if (color3.green() > 100) {
                return IndexerState.CENTER;
            }
        } else if (checkPos(5*TicksPerRot/6, 50)) {
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
        if (Math.abs(ticks-currticks%TicksPerRot) < tolerance) {
            return true;
        } else {
            return false;
        }
    }

    private Command oneRot() {
        return new RunToPosition(indexerCalculator, indexer.getRawTicks()+TicksPerRot).requires(this);
    }

    private void spinIndexer(double power){
        INSTANCE.indexer.setPower(power);
    }

}

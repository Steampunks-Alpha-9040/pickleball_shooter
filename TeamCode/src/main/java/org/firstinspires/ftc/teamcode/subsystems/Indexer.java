package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.controller.calculation.pid.DoubleComponent;
import dev.frozenmilk.dairy.core.util.controller.implementation.DoubleController;
import dev.frozenmilk.dairy.core.util.supplier.numeric.CachedMotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Indexer extends SDKSubsystem {

    private static final Indexer INSTANCE = new Indexer();

    public static Indexer getInstance(){
        return INSTANCE;
    }

    private enum BallPosition{
        ARM, OUTER, INNER
    }

    private enum BallColor{
        GREEN, PURPLE, NONE
    }

    private final HashMap<BallPosition, BallColor> ballMap = new HashMap<>(3);

    private final SubsystemObjectCell<DcMotorEx> indexer = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, ""));
    private final SubsystemObjectCell<ColorSensor> armColor = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, ""));
    private final SubsystemObjectCell<ColorSensor> secondColor = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, ""));
    private final SubsystemObjectCell<ColorSensor> thirdColor = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, ""));

    public DcMotorEx getIndexer(){
        return INSTANCE.indexer.get();
    }

    public ColorSensor getArmColor(){
        return INSTANCE.armColor.get();
    }

    public ColorSensor getSecondColor(){
        return INSTANCE.secondColor.get();
    }

    public ColorSensor getThirdColor(){
        return INSTANCE.thirdColor.get();
    }

    private double setpointPos = 0.0;

    private final CachedMotionComponentSupplier<Double> targetPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return setpointPos;
        }
        return Double.NaN;
    });

    private final CachedMotionComponentSupplier<Double> currentPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return getIndexer().getCurrentPosition() * Util.GoBILDA.RPM_1150.getCPR();
        }
        return Double.NaN;
    });

    private final CachedMotionComponentSupplier<Double> tolerancePosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return Constants.TurretConstants.turretTolerance;
        }
        return Double.NaN;
    });


    private final Cell<DoubleController> indexerPIDController = subsystemCell(() ->
            new DoubleController(
                    targetPosSupplier,
                    currentPosSupplier,
                    tolerancePosSupplier,
                    (Double power) -> {
                        getIndexer().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, Constants.IndexerConstants.indexer_kP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, Constants.IndexerConstants.indexer_kI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, Constants.IndexerConstants.indexer_kD))
                            .plus(new DoubleComponent.FF(MotionComponents.STATE, Constants.IndexerConstants.indexer_kF))
            )
    );




    public Indexer(){}

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode){

    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode){

    }

    @Override
    public void cleanup(@NonNull Wrapper opMode){

    }


    public Command spinIndexer(){
        return new Lambda("rotateIndexer")
                .setRequirements(INSTANCE)
                .setInit(() -> setIndexerPower(0.0));
    }
    public Command rotateIndexer(){
        return new Lambda("rotateIndexer")
                .setRequirements(INSTANCE)
                .setInit(() -> setIndexerPosition(0.0));
    }


    /**
     * @param power is in duty cycle (range 0.0-1.0)
     */
    private void setIndexerPower(double power){
        getIndexer().setPower(power);
    }

    /**
     * @param setpoint is in rotations of the indexer gear
     */
    private void setIndexerPosition(double setpoint){
        setpointPos = setpoint;
    }

    /**
     * updates the hashmap with the latest results from the
     */
    private void updateBallMap(){
        if (getColor(getArmColor()) != BallColor.NONE) {
            ballMap.put(BallPosition.ARM, getColor(getArmColor()));
        }
        if (getColor(getSecondColor()) != BallColor.NONE){
            ballMap.put(BallPosition.OUTER, getColor(getSecondColor()));
        }
        if (getColor(getSecondColor()) != BallColor.NONE){
            ballMap.put(BallPosition.INNER, getColor(getThirdColor()));
        }
    }

    private BallColor getColor(ColorSensor sensor){
        return BallColor.NONE;
    }

    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Indexer.Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }
}

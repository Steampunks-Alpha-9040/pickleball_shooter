package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.controller.calculation.pid.DoubleComponent;
import dev.frozenmilk.dairy.core.util.controller.implementation.DoubleController;
import dev.frozenmilk.dairy.core.util.supplier.numeric.CachedMotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;

public class Indexer implements Subsystem {

    public static final Indexer INSTANCE = new Indexer();

    private final SubsystemObjectCell<DcMotorEx> indexer = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.IndexerConstants.indexer)
    );

    private final SubsystemObjectCell<ColorSensor> feederSensor = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, Constants.IndexerConstants.feederSensor)
    );
    private final SubsystemObjectCell<ColorSensor> rampRightSensor = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, Constants.IndexerConstants.rampRightSensor)
    );
    private final SubsystemObjectCell<ColorSensor> rampLeftSensor = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(ColorSensor.class, Constants.IndexerConstants.rampLeftSensor)
    );

    private CachedMotionComponentSupplier<Double> targetIndexerPos;
    private CachedMotionComponentSupplier<Double> currentIndexerPos;
    private CachedMotionComponentSupplier<Double> toleranceIndexerPos;
    private final Cell<DoubleController> indexerPIDF = subsystemCell(
            () -> new DoubleController(
                    targetIndexerPos,
                    currentIndexerPos,
                    toleranceIndexerPos,
                    (Double power) -> {
                        INSTANCE.indexer.get().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, Constants.IndexerConstants.indexer_kP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, Constants.IndexerConstants.indexer_kI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, Constants.IndexerConstants.indexer_kD))
            )

    );



    public Command spinIndexer(){
        return new Lambda("spinIndexer").
                setInit(() -> spinIndexer(1));
    }

    private void spinIndexer(double power){
        INSTANCE.indexer.get().setPower(power);
    }

    private void setIndexerPosition(double pos){
         targetIndexerPos = new CachedMotionComponentSupplier<Double>(motionComponents -> {
            if (motionComponents == MotionComponents.STATE){
                return pos;
            }
            return Double.NaN;
         });


    }


    private Dependency<?> dependency = new SingleAnnotation<>(Indexer.Attach.class);
    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }
    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {}
}

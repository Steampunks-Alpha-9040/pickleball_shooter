package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponentSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;

public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();



    private final SubsystemObjectCell<DcMotorEx> flywheel = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.FlywheelConstants.flywheelName)
    );

    private MotionComponentSupplier<Double> targetVelocity;

    private final MotionComponentSupplier<Double> curretVelocity = (motionComponents -> {
        if (motionComponents == MotionComponents.STATE){
            return INSTANCE.flywheel.get().getVelocity();
        }
        return Double.NaN;
    });
    private final MotionComponentSupplier<Double> toleranceVelocity = (motionComponents -> {
        if (motionComponents == MotionComponents.STATE){
            return Constants.FlywheelConstants.flywheelVelocityTolerance;
        }
        return Double.NaN;
    });


    private Cell<DoubleController> flywheelPID = subsystemCell(
            () -> new DoubleController(
                    targetVelocity,
                    curretVelocity,
                    toleranceVelocity,
                    INSTANCE::setFlywheelPower,
                    new DoubleComponent.P(MotionComponents.STATE, Constants.FlywheelConstants.kP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, Constants.FlywheelConstants.kI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, Constants.FlywheelConstants.kD))
                            .plus(new DoubleComponent.FF(MotionComponents.STATE, Constants.FlywheelConstants.kF))
            )
    );

    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        flywheel.get().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.get().setDirection(DcMotorSimple.Direction.FORWARD);
        flywheelPID.get().setEnabled(false);
    }

    @Override
    public void preUserStartHook(@NonNull Wrapper opMode) {
        flywheelPID.get().setEnabled(true);
    }

    public Command runFlywheel(){
        return new Lambda("runFlywheel")
                .setInit(() -> INSTANCE.flywheel.get().setPower(1.0));
    }

    public Command stopFlywheel(){
        return new Lambda("stopFlywheel")
                .setInit(() -> INSTANCE.flywheel.get().setPower(0.0));
    }

    private void setFlywheelPower(double power){
        flywheelPID.get().setEnabled(true);
        targetVelocity = (motionComponents) -> {
            if (motionComponents == MotionComponents.STATE){
                return power;
            }
            return Double.NaN;
        };
    }




    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach{}
    //Dependencies for Mercurial
    private Dependency<?> dependency = Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));
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

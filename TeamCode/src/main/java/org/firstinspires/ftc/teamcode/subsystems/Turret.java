package org.firstinspires.ftc.teamcode.subsystems;


import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
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
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedDoubleSupplier;
import dev.frozenmilk.dairy.core.util.supplier.numeric.MotionComponents;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;


public class Turret extends SDKSubsystem {

    private static Turret INSTANCE;


    private double currentPos = 0.0;
    private double setpointPos = 0.0;

    private final SubsystemObjectCell<CRServo> turretServoM =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretMasterName));

    private final SubsystemObjectCell<CRServo> turretServoS =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretSlaveName));

    private final SubsystemObjectCell<AnalogInput> encoder =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(AnalogInput.class, Constants.TurretConstants.encoderName));

    private final Cell<EnhancedDoubleSupplier> currentAbsolutePosition = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) getEncoder().getVoltage() / 3.2 * 360));

    private double previousAbsolutePosition = 0.0;


    private final CachedMotionComponentSupplier<Double> targetPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return setpointPos;
        }
        return Double.NaN;
    });

    private final CachedMotionComponentSupplier<Double> currentPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return currentPos;
        }
        return Double.NaN;
    });

    private final CachedMotionComponentSupplier<Double> tolerancePosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return Constants.TurretConstants.turretTolerance;
        }
        return Double.NaN;
    });


    private final Cell<DoubleController> turretPIDController = subsystemCell(() ->
            new DoubleController(
                    targetPosSupplier,
                    currentPosSupplier,
                    tolerancePosSupplier,
                    (Double power) -> {
                        getTurretMaster().setPower(power);
                        getTurretSlave().setPower(power);
                    },
                    new DoubleComponent.P(MotionComponents.STATE, Constants.TurretConstants.turret_kP)
                            .plus(new DoubleComponent.I(MotionComponents.STATE, Constants.TurretConstants.turret_kI))
                            .plus(new DoubleComponent.D(MotionComponents.STATE, Constants.TurretConstants.turret_kD))
                            .plus(new DoubleComponent.FF(MotionComponents.STATE, Constants.TurretConstants.turret_kF))
            )
    );


    public Turret() {

    }

    public static CRServo getTurretMaster(){
        return INSTANCE.turretServoM.get();
    }

    public static CRServo getTurretSlave(){
        return INSTANCE.turretServoS.get();
    }

    public static AnalogInput getEncoder(){
        return INSTANCE.encoder.get();
    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(doNothing());
    }


    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode){
        updateTurretPosition();
    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}


    @NonNull
    public Command setTurretAngle(double angle){
        return new Lambda("turretFollowTag")
                .addRequirements(INSTANCE)
                .setInit(() -> setTurretTarget(angle));
    }

    @NonNull
    public Command doNothing(){
        return new Lambda("doNothingTurret");
    }

    public void setTurretTarget(double turretTarget){
        setpointPos = turretTarget;
    }

    public void updateTurretPosition(){
        if (Math.abs((currentAbsolutePosition.get().state() - previousAbsolutePosition)) > 355){ //if we changed by 355 deg in one tick, that means we probably went a revolution
            currentPos++;
        }
        previousAbsolutePosition = currentAbsolutePosition.get().state(); //sets the previous after
        currentPos += currentAbsolutePosition.get().state();
    }

    public double getCurrentPosition(){
        return currentPos * Constants.TurretConstants.servoToTurret;
    }


    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Turret.Attach.class));

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

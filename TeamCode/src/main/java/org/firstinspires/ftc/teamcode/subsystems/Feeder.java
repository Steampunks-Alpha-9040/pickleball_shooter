package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

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

public class Feeder extends SDKSubsystem {

    private static final Feeder INSTANCE = new Feeder();

    public static Feeder getInstance(){
        return INSTANCE;
    }


    private final SubsystemObjectCell<CRServo> omniFeeder = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.FeederConstants.feederArm));
    private final SubsystemObjectCell<CRServo> topFeeder = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.FeederConstants.feederBottomOmni));

    private final SubsystemObjectCell<Servo> arm = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(Servo.class, Constants.FeederConstants.feederTopWheel));
    private final SubsystemObjectCell<AnalogInput> armEncoder = subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(AnalogInput.class, Constants.FeederConstants.feederArmEncoder));

    private static Servo getArm() {return INSTANCE.arm.get();}
    private static AnalogInput getArmEncoder() {return INSTANCE.armEncoder.get();}

    private static CRServo getOmniFeeder() {return INSTANCE.omniFeeder.get();}
    private static CRServo getTopFeeder() {return INSTANCE.topFeeder.get();}

    private final Cell<EnhancedDoubleSupplier> armPosition = subsystemCell(
            () -> new EnhancedDoubleSupplier(() -> (double) getArmEncoder().getVoltage() * 3.2 * 360)
    );

    private double setpointPos = 0.0;


    private final CachedMotionComponentSupplier<Double> targetPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return setpointPos;
        }
        return Double.NaN;
    });

    private final CachedMotionComponentSupplier<Double> currentPosSupplier = new CachedMotionComponentSupplier<>(motionComponents -> {
        if (motionComponents == MotionComponents.STATE) {
            return armPosition.get().state();
        }
        return Double.NaN;
    });

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        getOmniFeeder().setDirection(DcMotorSimple.Direction.FORWARD);
        getTopFeeder().setDirection(DcMotorSimple.Direction.REVERSE);
        setDefaultCommand(doNothing());
    }


    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode){

    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}


    @NonNull
    public Command doNothing(){
        return new Lambda("doNothingFeeder");
    }

    @NonNull
    public Command moveArm(double setpoint){
        return new Lambda("moveArm")
                .setInit(() -> moveArmServo(setpoint));
    }

    @NonNull
    public Command startFeeder(){
        return new Lambda("startFeeder")
                .setInit(this::spinFeederWheels);
    }

    @NonNull
    public Command stopFeeder(){
        return new Lambda("stopFeeder")
                .setInit(this::stopFeederWheels);
    }

    private void moveArmServo(double setpoint){
        setpointPos = setpoint;
        if ((armPosition.get().state() - setpoint) > 0){
            getArm().setDirection(Servo.Direction.FORWARD);
        } else {
            getArm().setDirection(Servo.Direction.REVERSE);
        }
        getArm().setPosition(setpoint);
    }

    private void spinFeederWheels(){
        getOmniFeeder().setPower(0.5);
        getTopFeeder().setPower(0.5);
    }
    private void stopFeederWheels(){
        getOmniFeeder().setPower(0.0);
        getTopFeeder().setPower(0.0);
    }

    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Feeder.Attach.class));

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

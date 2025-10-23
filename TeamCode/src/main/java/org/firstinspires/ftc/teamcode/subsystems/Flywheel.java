package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
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
import dev.frozenmilk.dairy.core.util.supplier.numeric.EnhancedDoubleSupplier;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class Flywheel extends SDKSubsystem {
    private static final Flywheel INSTANCE = new Flywheel();

    private final SubsystemObjectCell<DcMotorEx> turretFlywheelMotor =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.TurretConstants.flywheelName));



    public static Flywheel getInstance(){
        return INSTANCE;
    }

    public static DcMotorEx getFlywheel() {
        return INSTANCE.turretFlywheelMotor.get();
    }


    private final Cell<EnhancedDoubleSupplier> current = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) getFlywheel().getCurrent(CurrentUnit.AMPS)));
    private final Cell<EnhancedDoubleSupplier> position = subsystemCell(() -> new EnhancedDoubleSupplier(() -> (double) getFlywheel().getCurrentPosition() * Util.GoBILDA.BARE.getCPR()));


    private Flywheel(){}

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(doNothing());
    }


    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode){

    }

    public void logFlywheel(){
        LoggingPanels.getPanelsManager().getTelemetry().addData("Flywheel/Velo", position.get().velocity());
        LoggingPanels.getPanelsManager().getTelemetry().addData("Flywheel/Current", current.get().state());
    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public Command runShooter() {
        return new Lambda("runShooter")
                .addRequirements(INSTANCE)
                .setInit(() -> getFlywheel().setPower(0.4));
    }

    @NonNull
    public Command stopShooter(){
        return new Lambda("Stop Shooter")
                .addRequirements(INSTANCE)
                .setInit(() -> setFlywheelPower(0.0));
    }

    @NonNull
    public Command doNothing(){
        return new Lambda("doNothingShooter")
                .addRequirements(INSTANCE);
    }


    /**
     * @param power is in duty cycle (range 0.0-1.0)
     */
    public void setFlywheelPower(double power){
        getFlywheel().setPower(power);
    }

    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Flywheel.Attach.class));

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

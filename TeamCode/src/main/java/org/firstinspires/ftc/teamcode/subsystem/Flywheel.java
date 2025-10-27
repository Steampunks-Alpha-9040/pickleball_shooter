package org.firstinspires.ftc.teamcode.subsystem;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;

public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();



    private final SubsystemObjectCell<DcMotorEx> flywheel = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.FlywheelConstants.flywheelName)
    );

    public Command runFlywheel(){
        return new Lambda("runFlywheel")
                .setInit(() -> INSTANCE.flywheel.get().setPower(-1.0));
    }

    public Command stopFlywheel(){
        return new Lambda("stopFlywheel")
                .setInit(() -> INSTANCE.flywheel.get().setPower(0.0));
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

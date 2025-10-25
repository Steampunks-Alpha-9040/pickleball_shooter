package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.BulkReads;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;

public class Feeder implements Subsystem {

    public static Feature INSTANCE = new Feeder();
    
    private SubsystemObjectCell<CRServo> bottomOmni = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.FeederConstants.feederBottomOmni)
    );

    private SubsystemObjectCell<CRServo> topOmni = subsystemCell(
            () -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.FeederConstants.feederTopWheel)
    );



    public



















    private Dependency<?> dependency = new SingleAnnotation<>(Feeder.Attach.class);
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

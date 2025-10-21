package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

public class LoggingPanels extends SDKSubsystem {

    private static final LoggingPanels INSTANCE = new LoggingPanels();

    private final Cell<PanelsTelemetry> panelsManager = subsystemCell(() -> PanelsTelemetry.INSTANCE);
    public static PanelsTelemetry getPanelsManager() {
        return INSTANCE.panelsManager.get();
    }



    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(LoggingPanels.Attach.class));

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

package org.firstinspires.ftc.teamcode.programs;

import com.bylazar.field.PanelsField;
import com.bylazar.telemetry.PanelsTelemetry;


import org.firstinspires.ftc.teamcode.subsystems.Feeder;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


public abstract class BaseOpMode extends NextFTCOpMode {

    protected PanelsTelemetry panels = PanelsTelemetry.INSTANCE;
    protected PanelsField field = PanelsField.INSTANCE;
    protected Feeder feeder = Feeder.INSTANCE;
    public BaseOpMode(){
        addComponents(
                new SubsystemComponent(
                        feeder
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }

}

package org.firstinspires.ftc.teamcode.programs;

import com.bylazar.field.PanelsField;
import com.bylazar.telemetry.PanelsTelemetry;



import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


public abstract class BaseOpMode extends NextFTCOpMode {

    protected PanelsTelemetry panels = PanelsTelemetry.INSTANCE;
    protected PanelsField field = PanelsField.INSTANCE;
    protected Flywheel flywheel = Flywheel.INSTANCE;
    protected Drivebase drivebase = Drivebase.INSTANCE;
//    protected Indexer indexer = Indexer.INSTANCE;
//    protected Feeder feeder = Feeder.INSTANCE;
//    protected Intake intake = Intake.INSTANCE;
    protected Turret turret = Turret.INSTANCE;
//    protected Vision vision = Vision.INSTANCE;

    public BaseOpMode(){
        addComponents(
                new SubsystemComponent(
                        drivebase,
//                        indexer,
//                        feeder,
                        flywheel,
//                        intake,
                        turret
//                        vision
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }

}

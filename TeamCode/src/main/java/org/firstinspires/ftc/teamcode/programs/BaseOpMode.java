package org.firstinspires.ftc.teamcode.programs;

import com.bylazar.field.PanelsField;
import com.bylazar.telemetry.PanelsTelemetry;



import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.pedropathing.Constants;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


public abstract class BaseOpMode extends NextFTCOpMode {

    protected PanelsTelemetry panels = PanelsTelemetry.INSTANCE;
    protected PanelsField field = PanelsField.INSTANCE;
    protected Flywheel flywheel = Flywheel.INSTANCE;

    protected Drivebase drivebase = Drivebase.INSTANCE;
    protected Indexer indexer = Indexer.INSTANCE;
    protected Feeder feeder = Feeder.INSTANCE;
    protected Intake intake = Intake.INSTANCE;
    protected Turret turret = Turret.INSTANCE;

    public BaseOpMode(){
        addComponents(
                new SubsystemComponent(
                        drivebase,
                        indexer,
                        feeder,
                        flywheel,
                        intake,
                        turret
                ),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );

    }

}

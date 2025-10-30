package org.firstinspires.ftc.teamcode.programs;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;

import dev.nextftc.ftc.NextFTCOpMode;


public abstract class BaseOpMode extends NextFTCOpMode {

    protected PanelsTelemetry panels = PanelsTelemetry.INSTANCE;
    protected Flywheel flywheel = Flywheel.INSTANCE;
    protected Drivebase drivebase = Drivebase.INSTANCE;
    protected Indexer indexer = Indexer.INSTANCE;
    protected Feeder feeder = Feeder.INSTANCE;

}

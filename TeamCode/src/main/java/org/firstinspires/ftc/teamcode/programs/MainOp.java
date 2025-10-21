package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.ftcontrol.LoopTimer;
import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.LoggingPanels;
import org.firstinspires.ftc.teamcode.util.Drawer;

import dev.frozenmilk.mercurial.commands.groups.Parallel;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private final PanelsTelemetry panelsManager = LoggingPanels.getPanelsManager();

    private final LoopTimer loopTimer = new LoopTimer();

    private final Drawer field = new Drawer();

    private final Parallel runOuttake = new Parallel(
        Feeder.getInstance().startFeeder()
    );

    @Override
    public void init() {
        telemetryManager.debug("hi world");
        telemetryManager.update(telemetry);
    }

    @Override
    public void loop() {
        loopTimer.start();





        loopTimer.end();
    }

    public void logFlywheel(){
        panelsManager.getTelemetry().addData("Flywheel/Velo", Flywheel.getInstance());
    }
}

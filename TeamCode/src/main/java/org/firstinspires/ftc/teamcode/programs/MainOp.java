package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.ftcontrol.LoopTimer;
import com.bylazar.ftcontrol.panels.Panels;
import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.LoggingPanels;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import dev.frozenmilk.mercurial.commands.groups.Parallel;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private final TelemetryManager telemetryManager = LoggingPanels.getTelemetryManager();

    private LoopTimer timer = new LoopTimer();

    private final Parallel runOuttake = new Parallel(
        Feeder.getInstance().startFeeder()
    );

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        timer.start();




        timer.end();
    }
}

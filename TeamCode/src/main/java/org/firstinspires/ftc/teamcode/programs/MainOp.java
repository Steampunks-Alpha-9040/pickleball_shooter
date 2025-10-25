package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.utils.LoopTimer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.LoggingPanels;
import org.firstinspires.ftc.teamcode.util.Drawer;

import dev.frozenmilk.mercurial.commands.groups.Parallel;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private static final Logger log = LoggerFactory.getLogger(MainOp.class);
    private final PanelsTelemetry panelsManager = PanelsTelemetry.INSTANCE;

    private ElapsedTime timer = new ElapsedTime();
    private LoopTimer loopTimer = new LoopTimer();

    @Override
    public void init() {
        panelsManager.getTelemetry().debug("hi world");
        panelsManager.getTelemetry().update(telemetry);
    }

    @Override
    public void loop() {
        loopTimer.start();

    }

    public void logFlywheel(){

    }
}

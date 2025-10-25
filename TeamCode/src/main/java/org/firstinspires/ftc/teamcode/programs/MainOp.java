package org.firstinspires.ftc.teamcode.programs;


import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.sin;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.utils.LoopTimer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    }

    public void logFlywheel(){

    }
}

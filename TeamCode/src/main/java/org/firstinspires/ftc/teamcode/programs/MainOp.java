package org.firstinspires.ftc.teamcode.programs;


import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.sin;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private static final Logger log = LoggerFactory.getLogger(MainOp.class);
    private final PanelsTelemetry panelsManager = PanelsTelemetry.INSTANCE;

    private ElapsedTime timer = new ElapsedTime();


    @Override
    public void init() {
        panelsManager.getTelemetry().debug("hi world");
        panelsManager.getTelemetry().update(telemetry);
    }

    @Override
    public void loop() {
        logFlywheel();

        panelsManager.getTelemetry().update(telemetry);

    }

    public void logFlywheel(){
        double t = timer.seconds();
        double sinVariable = sin(t);
        double lissajous = sin(3 * t + Math.PI / 2) * cos(2 * t);

        panelsManager.getTelemetry().addData("sin", sinVariable);
        panelsManager.getTelemetry().addData("lissajous", lissajous);
    }
}

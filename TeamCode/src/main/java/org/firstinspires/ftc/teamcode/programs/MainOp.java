package org.firstinspires.ftc.teamcode.programs;


import static java.lang.Math.cos;
import static java.lang.Math.exp;
import static java.lang.Math.sin;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.utils.LoopTimer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.Flywheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.frozenmilk.mercurial.Mercurial;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    @Override
    public void init() {
        Mercurial.gamepad1().x().onTrue(Flywheel.INSTANCE.runFlywheel());
        Mercurial.gamepad1().y().onTrue(Flywheel.INSTANCE.stopFlywheel());
    }

    @Override
    public void loop() {

    }

    public void logFlywheel(){

    }
}

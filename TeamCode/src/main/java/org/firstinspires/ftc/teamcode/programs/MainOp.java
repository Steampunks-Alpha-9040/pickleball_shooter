package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Flywheel;

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

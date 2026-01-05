package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;

import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.Gamepads;

import org.firstinspires.ftc.robotcore.internal.hardware.android.GpioPin;
import org.firstinspires.ftc.teamcode.Constants;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOpBlue", group = "TeleOp")
public class TeleOpBLUE extends BaseOpMode {
    private LoopTimer timer = new LoopTimer();

    public TeleOpBLUE() {
        super();
    }


    @Override
    public void onInit() {
        Constants.OpModeConstants.side = Constants.Side.BLUE;

        field.getField().setStyle("none", "white", 1.5);

        drivebase.getMecanumDriver().schedule();


        drivebase.setIMUHeading(180);
        drivebase.zeroTurretQuadature();
        drivebase.zeroHoodQuadature();

    }

    @Override
    public void onUpdate(){
        ActiveOpMode.telemetry().update();
    }
}
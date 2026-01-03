package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.ftc.Gamepads;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;


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


    }

    @Override
    public void onUpdate() {

    }
}


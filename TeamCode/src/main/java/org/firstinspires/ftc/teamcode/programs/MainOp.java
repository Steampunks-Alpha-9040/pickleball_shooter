package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {


    public MainOp(){
        addComponents(
                new SubsystemComponent(
                        super.feeder
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {



        Gamepads.gamepad2().b().whenBecomesTrue(
                feeder.setArmUp()
        );

    }

    @Override
    public void onUpdate(){

    }

}

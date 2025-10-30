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
                        super.flywheel,
                        super.drivebase,
                        super.indexer,
                        super.feeder
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {


        drivebase.getRobotMecanumDriver().schedule();



        Gamepads.gamepad1().a().whenBecomesTrue(
                flywheel.shootFlywheelFar()
        );
        Gamepads.gamepad1().b().whenBecomesTrue(
                flywheel.shootFlywheelClose()
        );
        Gamepads.gamepad1().x().whenBecomesTrue(
                flywheel.stopFlywheel()
        );


        Gamepads.gamepad2().b().whenTrue(
                indexer.spinIndexer()
        );
        Gamepads.gamepad2().a().whenTrue(
                feeder.transfer()
        );
        Gamepads.gamepad2().x().whenTrue(
                feeder.store()
        );

    }

    @Override
    public void onUpdate(){

    }

}

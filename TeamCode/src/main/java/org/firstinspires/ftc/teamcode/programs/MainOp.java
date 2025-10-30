package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Flywheel;

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
                        super.indexer
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {
        drivebase.getMecanumDriver().schedule();

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

    }

    @Override
    public void onUpdate(){

    }

}

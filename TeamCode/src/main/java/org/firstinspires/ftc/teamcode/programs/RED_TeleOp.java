package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;


@TeleOp(name = "RED_PickleOp")
public class RED_TeleOp extends BaseOpMode {


    public RED_TeleOp(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase,
                        super.indexer,
                        super.feeder,
                        super.flywheel,
                        super.intake
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit() {

        drivebase.getMecanumDriver().schedule();

        Gamepads.gamepad1().y().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        new ParallelGroup(
                                indexer.spinIndexer(),
                                feeder.transfer()
                        )
                ).whenBecomesFalse(
                        new ParallelGroup(
                                indexer.stopIndexer(),
                                feeder.store()
                        )
                );

        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.transfer()
                )
                .whenBecomesFalse(
                        feeder.store()
                );
        Gamepads.gamepad1().x().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        flywheel.stopFlywheel()
                ).whenBecomesFalse(
                        flywheel.shootFlywheelFar()
                );
        Gamepads.gamepad1().a().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        intake.spinIntake()
                ).whenBecomesFalse(
                        intake.stopIntake()
                );
        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
                flywheel.spinHoodUp()
        ).whenBecomesFalse(
                flywheel.stopHood()
        );
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(
                flywheel.spinHoodDown()
        ).whenBecomesFalse(
                flywheel.stopHood()
        );

        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                turret.spinTurretRight()
        ).whenBecomesFalse(
                turret.stopTurret()
        );
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(
                turret.spinTurretLeft()
        ).whenBecomesFalse(
                turret.stopTurret()
        );

    }

    @Override
    public void onUpdate(){
        flywheel.log(telemetry);
        telemetry.update();
    }

}

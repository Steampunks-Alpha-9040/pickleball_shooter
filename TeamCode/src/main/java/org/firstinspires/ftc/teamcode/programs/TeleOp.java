package org.firstinspires.ftc.teamcode.programs;


import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOp")
public class TeleOp extends BaseOpMode {


    public TeleOp(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase,
                        super.indexer,
                        super.feeder,
                        super.flywheel,
                        super.intake,
                        super.vision
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

        Gamepads.gamepad1().rightTrigger().greaterThan(0.5)
                .whenBecomesFalse(
                        flywheel.stopFlywheel()
                ).whenBecomesTrue(
                        flywheel.shootFlywheelFar()
                );
        Gamepads.gamepad1().a().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        intake.spinIntake()
                ).whenBecomesFalse(
                        intake.stopIntake()
                );
        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
                turret.spinTurretRight()
        ).whenBecomesFalse(
                turret.stopTurret()
        );
        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
                turret.spinTurretLeft()
        ).whenBecomesFalse(
                turret.stopTurret()
        );
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
                turret.trackTurret()
        ).whenBecomesFalse(
                turret.stopTurret()
        );
    }

    @Override
    public void onUpdate(){
        flywheel.log(telemetry);
//        vision.logVision(telemetry);
        telemetry.update();
    }

}

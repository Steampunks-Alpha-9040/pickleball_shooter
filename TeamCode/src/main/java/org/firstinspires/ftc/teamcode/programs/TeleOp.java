package org.firstinspires.ftc.teamcode.programs;


import com.arcrobotics.ftclib.util.Timing;
import com.pedropathing.util.Timer;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOp")
public class TeleOp extends BaseOpMode {

    private Timer timer;

    public TeleOp(){
        super();
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

        Gamepads.gamepad1().x().toggleOnBecomesTrue()
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

        timer = new Timer();
        timer.resetTimer();
    }

    @Override
    public void onUpdate(){
        flywheel.log(telemetry);
        telemetry.addData("tyasdf", vision.getHorizontalTy());
        //        vision.logVision(telemetry);


        telemetry.addData("timer", timer.getElapsedTime());
        telemetry.update();
        timer.resetTimer();

    }

}

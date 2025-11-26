package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.ftc.Gamepads;
import com.bylazar.field.PanelsField;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOp")
public class TeleOp extends BaseOpMode {

    private LoopTimer timer = new LoopTimer();

    public TeleOp() {
        super();
    }


    @Override
    public void onInit() {

        field.getField().setStyle("none", "white", 1.5);
        field.getField().update();


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
        Gamepads.gamepad1().b().and(
                Gamepads.gamepad1().rightTrigger().greaterThan(0.2)
        ).whenBecomesTrue(
                drivebase.zeroGryo()
        );
    }

    @Override
    public void onUpdate() {
        timer.start();

        // Draw dot at current animated position
        field.getField().moveCursor(drivebase.getBotpose().getX(DistanceUnit.INCH), drivebase.getBotpose().getY(DistanceUnit.INCH));
        field.getField().circle(1.5);

        flywheel.log(panels);
        panels.getTelemetry().addData("quad", drivebase.getTurretQuadature());
        panels.getTelemetry().addData("botpose", drivebase.getBotpose());
        timer.end();
        panels.getTelemetry().addData("LoopTime", timer.getMs());
        field.getField().update();
        panels.getTelemetry().update();
    }
}


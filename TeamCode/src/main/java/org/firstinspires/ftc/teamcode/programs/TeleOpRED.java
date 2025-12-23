package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.ftc.Gamepads;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOpRED", group = "TeleOp")
public class TeleOpRED extends BaseOpMode {
    private LoopTimer timer = new LoopTimer();

    public TeleOpRED() {
        super();
    }


    @Override
    public void onInit() {
        Constants.OpModeConstants.side = Constants.Side.RED;

        field.getField().setStyle("none", "white", 1.5);

        drivebase.setStartingPose(72,72);

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
                        flywheel.shootFlywheel()
                );
        Gamepads.gamepad1().a().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        intake.spinIntake()
                ).whenBecomesFalse(
                        intake.stopIntake()
                );
//        Gamepads.gamepad1().dpadRight().whenBecomesTrue(
//                turret.spinTurretRight()
//        ).whenBecomesFalse(
//                turret.stopTurret()
//        );
//        Gamepads.gamepad1().dpadLeft().whenBecomesTrue(
//                turret.spinTurretLeft()
//        ).whenBecomesFalse(
//                turret.stopTurret()
//        );
        Gamepads.gamepad1().rightBumper().whenBecomesTrue(
                turret.trackTurret()
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
//        field.getField().moveCursor(Vision.INSTANCE.getRaw2D().getY(DistanceUnit.INCH), Vision.INSTANCE.getRaw2D().getX(DistanceUnit.INCH));
        field.getField().moveCursor(drivebase.getBotpose().getX(DistanceUnit.INCH), drivebase.getBotpose().getY(DistanceUnit.INCH)); //flipped since x in pedro is y
        field.getField().circle(1.5);

        flywheel.log(panels);
        panels.getTelemetry().addData("quad", drivebase.getTurretQuadature());
        panels.getTelemetry().addData("botpose", Util.poseUnitConvertor(DistanceUnit.METER,drivebase.getBotpose()));
        timer.end();
        panels.getTelemetry().addData("LoopTime", timer.getMs());
        field.getField().update();
        panels.getTelemetry().update();
        telemetry.update();
    }
}


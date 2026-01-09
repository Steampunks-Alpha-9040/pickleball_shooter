package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOpRED", group = "TeleOp")
public class TeleOpRED extends BaseOpMode {
    private LoopTimer timer = new LoopTimer();

    public TeleOpRED() {
        super();
    }


    @Override
    public void onInit() {

        field.getField().setStyle("none", "white", 1.5);

        drivebase.setFollower(PedroComponent.follower());
        drivebase.getFollower().setStartingPose(new Pose(72, 7, Math.toRadians(90)));

        turret.setSide(Constants.Side.RED);

        drivebase.getMecanumDriver().schedule();

        Gamepads.gamepad1().y().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.turnWheelsOn()
                ).whenBecomesFalse(
                        feeder.turnWheelsOff()
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
        Gamepads.gamepad1().b().and(
                Gamepads.gamepad1().rightTrigger().greaterThan(0.2)
        ).whenBecomesTrue(
                drivebase.zeroGryo()
        );

//        Gamepads.gamepad2().a().whenBecomesTrue(
//                indexer.autoSet()
//        );
//
        Gamepads.gamepad2().x().whenBecomesTrue(
                indexer.oneRot()
        );
        drivebase.zeroHoodQuadature();
        drivebase.zeroTurretQuadature();

    }

    @Override
    public void onUpdate() {
        timer.start();

        // Draw dot at current animated position
//        field.getField().moveCursor(Vision.INSTANCE.getRaw2D().getY(DistanceUnit.INCH), Vision.INSTANCE.getRaw2D().getX(DistanceUnit.INCH));
        panels.getTelemetry().addData("LoopTime", timer.getMs());
        field.getField().update();
        panels.getTelemetry().update();
        telemetry.update();
    }
}


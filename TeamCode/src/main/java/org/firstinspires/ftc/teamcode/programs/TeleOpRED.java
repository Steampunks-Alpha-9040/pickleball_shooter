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
        drivebase.getFollower().setStartingPose(new Pose(80.3, 8.69, Math.toRadians(90)));

        turret.setSide(Constants.Side.RED);

        drivebase.getMecanumDriver().schedule();

        //Gamepad 1
        Gamepads.gamepad1().rightBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        intake.spinIntake()
                ).whenBecomesFalse(
                        intake.stopIntake()
                );

        Gamepads.gamepad1().leftBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        indexer.spinIndexerFast()
                ).whenBecomesFalse(
                        indexer.stopIndexer()
                );

        Gamepads.gamepad1().x().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        drivebase.resetPose(1)
                );

        //Gamepad 2
        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.turnWheelsOn()
                ).whenBecomesFalse(
                        feeder.turnWheelsOff()
                );

        Gamepads.gamepad2().x().toggleOnBecomesTrue()
                .whenBecomesFalse(
                        flywheel.stopFlywheel()
                ).whenBecomesTrue(
                        flywheel.shootFlywheel()
                );

        Gamepads.gamepad2().a().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        indexer.spinIndexerSlow()
                ).whenBecomesFalse(
                        indexer.stopIndexer()
                );
        Gamepads.gamepad2().y().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.setArmDown()
                ).whenBecomesFalse(
                        feeder.setArmUp()
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


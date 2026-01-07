package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;
import com.pedropathing.geometry.Pose;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOpBlue", group = "TeleOp")
public class TeleOpBLUE extends BaseOpMode {
    private LoopTimer timer = new LoopTimer();

    public TeleOpBLUE() {
        super();
    }


    @Override
    public void onInit() {
        Constants.OpModeConstants.side = Constants.Side.BLUE;

        field.getField().setStyle("none", "white", 1.5);

        drivebase.setFollower(PedroComponent.follower());
        drivebase.getFollower().setStartingPose(new Pose(72, 7, Math.toRadians(90)));

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
//        Gamepads.gamepad2().x().whenBecomesTrue(
//                indexer.oneRot()
//        );
        drivebase.zeroHoodQuadature();
        drivebase.zeroTurretQuadature();

    }

    @Override
    public void onUpdate() {
        timer.start();

        // Draw dot at current animated position
//        field.getField().moveCursor(Vision.INSTANCE.getRaw2D().getY(DistanceUnit.INCH), Vision.INSTANCE.getRaw2D().getX(DistanceUnit.INCH));
        field.getField().moveCursor(drivebase.getFollower().getPose().getX(), drivebase.getFollower().getPose().getY()); //flipped since x in pedro is y
        field.getField().circle(1.5);

        panels.getTelemetry().addData("quad", drivebase.getTurretQuadature());
        timer.end();
        panels.getTelemetry().addData("LoopTime", timer.getMs());
        field.getField().update();
        panels.getTelemetry().update();
        telemetry.update();
    }
}


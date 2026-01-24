package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;
import com.pedropathing.geometry.CoordinateSystem;
import com.pedropathing.geometry.Pose;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.util.Util;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PickleOpBlue", group = "TeleOp")
public class TeleOpBLUE extends BaseOpMode {
    private LoopTimer timer = new LoopTimer();

    public TeleOpBLUE() {
        super();
    }


    @Override
    public void onInit() {


        field.getField().setStyle("none", "white", 1.5);

        drivebase.setFollower(PedroComponent.follower());
        // Auto ends on 63.307, 7.811, 90
//        drivebase.getFollower().setStartingPose(new Pose(63.307, 7.811, Math.toRadians(90)));
        drivebase.getFollower().setStartingPose(new Pose(72+Constants.robotWidth/2, 72-Constants.robotLength/2, Math.toRadians(90)));
        drivebase.INSTANCE.getBR().setCurrentPosition(0);
        drivebase.INSTANCE.getFL().setCurrentPosition(0);

        flywheel.setSide(Constants.Side.BLUE);
        turret.setSide(Constants.Side.BLUE);
        Constants.OpModeConstants.setSide(Constants.Side.BLUE);


        drivebase.getMecanumDriver(Constants.Side.BLUE).schedule();

//      For GamePlay

        //Gamepad 1
        Gamepads.gamepad1().rightBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        intake.spinIntake()
                ).whenBecomesFalse(
                        intake.stopIntake()
                );

        Gamepads.gamepad1().leftBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        indexer.spinIndexerSlow()
                ).whenBecomesFalse(
                        indexer.stopIndexer()
                );
        Gamepads.gamepad1().x().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        drivebase.resetPose(0)
                );


        //Gamepad 2
        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.turnWheelsOn()
                ).whenBecomesFalse(
                        feeder.turnWheelsOff()
                );

        Gamepads.gamepad2().x().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        flywheel.changeBool()
                ).whenBecomesFalse(
                        flywheel.changeBool()
                );
        Gamepads.gamepad2().y().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.setArmDown()
                ).whenBecomesFalse(
                        feeder.setArmUp()
                );
        Gamepads.gamepad2().rightBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        indexer.spinIndexerFast()
                ).whenBecomesFalse(
                        indexer.stopIndexer()
                );

        Gamepads.gamepad2().leftBumper().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        indexer.spinIndexerSlow()
                ).whenBecomesFalse(
                        indexer.stopIndexer()
                );


//        //Testing Controls
//
//        //Gamepad 1
//        Gamepads.gamepad1().rightBumper().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        intake.spinIntake()
//                ).whenBecomesFalse(
//                        intake.stopIntake()
//                );
//
//        Gamepads.gamepad1().y().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changePID()
//                );
//
//        Gamepads.gamepad1().a().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeTestHoodAngle(1)
//                ).whenBecomesFalse(
//                        flywheel.changeTestHoodAngle(1)
//                );
//
//        Gamepads.gamepad1().b().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeTestHoodAngle(-1)
//                ).whenBecomesFalse(
//                        flywheel.changeTestHoodAngle(-1)
//                );
//
//        Gamepads.gamepad1().leftBumper().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeTestHoodAngle(0.1)
//                ).whenBecomesFalse(
//                        flywheel.changeTestHoodAngle(0.1)
//                );
//
//        Gamepads.gamepad1().x().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeTestHoodAngle(-0.1)
//                ).whenBecomesFalse(
//                        flywheel.changeTestHoodAngle(-0.1)
//                );
//
//        //Gamepad 2
//        Gamepads.gamepad2().b().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        feeder.turnWheelsOn()
//                ).whenBecomesFalse(
//                        feeder.turnWheelsOff()
//                );
//
//        Gamepads.gamepad2().x().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.shootFlywheel()
//                ).whenBecomesFalse(
//                        flywheel.stopFlywheel()
//                );
//
//        Gamepads.gamepad2().a().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        indexer.spinIndexerSlow()
//                ).whenBecomesFalse(
//                        indexer.stopIndexer()
//                );
//        Gamepads.gamepad2().y().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        feeder.setArmDown()
//                ).whenBecomesFalse(
//                        feeder.setArmUp()
//                );
//        Gamepads.gamepad2().rightBumper().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeRPM(10)
//                ).whenBecomesFalse(
//                        flywheel.changeRPM(10)
//                );
//
//        Gamepads.gamepad2().leftBumper().toggleOnBecomesTrue()
//                .whenBecomesTrue(
//                        flywheel.changeRPM(-10)
//                ).whenBecomesFalse(
//                        flywheel.changeRPM(-10)
//                );


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


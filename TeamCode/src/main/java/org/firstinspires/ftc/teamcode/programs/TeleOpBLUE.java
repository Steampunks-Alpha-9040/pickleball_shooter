package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.utils.LoopTimer;

import dev.nextftc.core.commands.groups.ParallelGroup;
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

        drivebase.setStartingPose(96,10);

        drivebase.getMecanumDriver().schedule();

        Gamepads.gamepad2().dpadUp().whenBecomesTrue(
                flywheel.spinHoodUp()
        );
        Gamepads.gamepad2().dpadDown().whenBecomesTrue(
                flywheel.spinHoodDown()
        );

        Gamepads.gamepad1().dpadUp().whenBecomesTrue(
                flywheel.fasterFLywheel()
        );
        Gamepads.gamepad1().dpadDown().whenBecomesTrue(
                flywheel.slowerFLywheel()
        );

        Gamepads.gamepad1().a().toggleOnBecomesTrue().whenBecomesTrue(
                flywheel.shootFlywheel()
        ).whenBecomesFalse(
                flywheel.stopFlywheel()
        );

        drivebase.zeroTurretQuadature();
        drivebase.zeroHoodQuadature();

    }

    @Override
    public void onUpdate() {
        timer.start();

        // Draw dot at current animated position
//        field.getField().moveCursor(Vision.INSTANCE.getRaw2D().getY(DistanceUnit.INCH), Vision.INSTANCE.getRaw2D().getX(DistanceUnit.INCH));
        field.getField().moveCursor(drivebase.getBotpose().getX(DistanceUnit.INCH), drivebase.getBotpose().getY(DistanceUnit.INCH)); //flipped since x in pedro is y
        field.getField().circle(1.5);

        panels.getTelemetry().addData("quad", drivebase.getTurretQuadature());
        panels.getTelemetry().addData("botpose", Util.poseUnitConvertor(DistanceUnit.METER,drivebase.getBotpose()));
        timer.end();
        panels.getTelemetry().addData("LoopTime", timer.getMs());
        field.getField().update();
        panels.getTelemetry().update();
        telemetry.update();
    }
}


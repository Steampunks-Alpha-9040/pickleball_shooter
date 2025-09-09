package org.firstinspires.ftc.teamcode.programs;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ICBPL_robot;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends LinearOpMode {

    private ICBPL_robot bot;

    private GamepadEx drivePad;


    @Override
    public void runOpMode() throws InterruptedException {
        this.drivePad = new GamepadEx(gamepad1);
        this.bot = new ICBPL_robot(ICBPL_robot.OpModeType.TELEOP, hardwareMap, telemetry);


        telemetry.addData("ICBPL","Ready");
        telemetry.update();

        drivePad.getGamepadButton(GamepadKeys.Button.A)
                .whileHeld(bot.spinFlywheel())
                .whenReleased(bot.stopFlywheel());

        waitForStart();



        while (opModeIsActive()) {
            bot.run();
        }
    }
}

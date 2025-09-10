package org.firstinspires.ftc.teamcode.programs;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.teamcode.ICBPL_robot;

@TeleOp(name = "drive", group = "prototype")
public class DriveProto extends LinearOpMode {

    private ICBPL_robot bot;

    private GamepadEx drivePad;

    @Override
    public void runOpMode() throws InterruptedException {
        this.drivePad = new GamepadEx(gamepad1);
        this.bot = new ICBPL_robot(ICBPL_robot.OpModeType.INTAKEPROTO, hardwareMap, new TelemetryImpl(this), drivePad);


        telemetry.addData("ICBPL","Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            bot.run();
        }
    }
}

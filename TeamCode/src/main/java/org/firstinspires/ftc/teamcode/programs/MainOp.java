package org.firstinspires.ftc.teamcode.programs;


import com.bylazar.ftcontrol.panels.Panels;
import com.bylazar.ftcontrol.panels.integration.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.ICBPL_robot;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends LinearOpMode {

    private ICBPL_robot bot;


    private TelemetryManager panelsTelemetry = Panels.getTelemetry();



    @Override
    public void runOpMode() throws InterruptedException {
        this.drivePad = new GamepadEx(gamepad1);
        this.bot = new ICBPL_robot(ICBPL_robot.OpModeType.MainOP, hardwareMap, telemetry);


        telemetry.addData("ICBPL","Ready");
        telemetry.update();

        drivePad.getGamepadButton(GamepadKeys.Button.A);

        waitForStart();



        while (opModeIsActive()) {
            bot.run();
        }
    }
}

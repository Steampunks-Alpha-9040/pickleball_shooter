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

    }
}

package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

import dev.nextftc.core.commands.Command;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private final Command drive = super.drivebase.controllerDrive();

    @Override
    public void init() {
        drive.schedule();
    }

    @Override
    public void loop() {

    }

}

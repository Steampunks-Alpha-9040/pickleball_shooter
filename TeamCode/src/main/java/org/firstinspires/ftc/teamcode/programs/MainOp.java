package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.tools.javac.Main;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    private final Command drive = super.drivebase.getRobotMecanumDriver();

    public MainOp(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }




    @Override
    public void onStartButtonPressed() {
        drive.schedule();

    }

    @Override
    public void onUpdate() {

    }
}

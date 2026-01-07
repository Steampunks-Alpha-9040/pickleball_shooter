package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@TeleOp(name = "hai :3")
public class testDriving extends NextFTCOpMode {
    protected Drivebase drivebase = Drivebase.INSTANCE;

    public testDriving(){
        addComponents(
                new SubsystemComponent(
                        drivebase
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onInit(){
        drivebase.getMecanumDriver().schedule();

    }
}

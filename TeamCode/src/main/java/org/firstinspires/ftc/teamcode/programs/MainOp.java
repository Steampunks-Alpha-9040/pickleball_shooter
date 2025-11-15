package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.components.BulkReadComponent;

import org.firstinspires.ftc.teamcode.subsystems.ColorSensorSubsystem;

@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends BaseOpMode {

    @Override
    public void onInit() {
        // Initialize singleton
        new ColorSensorSubsystem(hardwareMap, telemetry, "sensor_color");

        drivebase.getMecanumDriver().schedule();

        Gamepads.gamepad1().y().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        new ParallelGroup(
                                indexer.spinIndexer(),
                                feeder.transfer()
                        )
                ).whenBecomesFalse(
                        new ParallelGroup(
                                indexer.stopIndexer(),
                                feeder.store()
                        )
                );

        Gamepads.gamepad2().b().toggleOnBecomesTrue()
                .whenBecomesTrue(
                        feeder.transfer()
                )
                .whenBecomesFalse(
                        feeder.store()
                );

        // Add components AFTER initializing the singleton
        addComponents(
                new SubsystemComponent(
                        drivebase,
                        indexer,
                        feeder,
                        ColorSensorSubsystem.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    @Override
    public void onUpdate() {
        // Example usage:
        String detected = ColorSensorSubsystem.INSTANCE.detectColor();
        telemetry.addData("ColorSensor", detected);
    }
}

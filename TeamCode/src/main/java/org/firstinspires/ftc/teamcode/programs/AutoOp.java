package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;

@Autonomous(name = "pickleAuto")
public class AutoOp extends BaseOpMode{
    public AutoOp(){
        super();
    }

//    public Command autonomousRoutine() {
//        return new ParallelGroup(
//                // keep flywheel spun up
//                flywheel.shootFlywheelFar(),
//
//                // do the actual feeding
//                new SequentialGroup(
//                        new Delay(1.0),              // let flywheel get up to speed
//                        feeder.setArmDown(),
//                        feeder.turnWheelsOn(),
//                        indexer.spinIndexer(),
//                        new Delay(10),
//                        feeder.turnWheelsOff(),
//                        indexer.stopIndexer(),
//                        flywheel.stopFlywheel(),
//                        new Delay(3),
//                        drivebase.driveForwardSimple(0.3,0.5)
//                )
//        );
//    }


    @Override
    public void onInit() {
        flywheel.stopFlywheel();
    }

    @Override
    public void onStartButtonPressed() {
        // 1) spin flywheel forever (or until you stop it later)
        flywheel.shootFlywheel().schedule();

        // 2) do the actual auto: shoot -> drive
        new SequentialGroup(
                new Delay(5.0),          // let flywheel get up to speed
                feeder.setArmDown(),
                feeder.turnWheelsOn(),
                indexer.spinIndexer(),
                new Delay(10),          // enough time to feed
                feeder.turnWheelsOff(),
                indexer.stopIndexer(),
                flywheel.stopFlywheel(),
                // 3) now DRIVE AFTER SHOOTING
                drivebase.driveForwardSimple(0.4, 1.6)

        ).schedule();
    }
}

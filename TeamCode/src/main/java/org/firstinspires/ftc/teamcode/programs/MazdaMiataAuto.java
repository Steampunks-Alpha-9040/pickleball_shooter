package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.components.BulkReadComponent;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierCurve;

import org.firstinspires.ftc.teamcode.util.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.components.BulkReadComponent;
@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class MazdaMiataAuto extends BaseOpMode {

    public MazdaMiataAuto(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase,
                        super.indexer,
                        super.feeder,
                        super.flywheel
                ),
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE

        );
    }

    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    private Timer pathTimer, opmodeTimer;

    @Override
    public void onInit() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        paths = new Paths(follower);
        follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onStartButtonPressed() {
        follower.update(); // Update Pedro Pathing
//        pathState = autonomousPathUpdate(); // Update autonomous state machine

        autonomousRoutine().schedule();
        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {

        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;

        public Paths(Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(44.000, 5.300), new Pose(44.000, 36.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(44.000, 36.000), new Pose(21.000, 36.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(21.000, 36.000), new Pose(44.000, 8.693))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(44.000, 8.693), new Pose(44.000, 60.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(44.000, 60.000), new Pose(21.000, 60.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(21.000, 60.000), new Pose(44.000, 8.693))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();
        }
    }


    double shootFarDelay = 1.0;
    double Beginning = 1.0;
    double intakeDelay = 1.0;

    public Command shoot() {
        return new ParallelGroup(
                feeder.setArmDown(),
                feeder.turnWheelsOn()
        );
    }

    public Command safeShoot() {
        if (indexer.checkValid()) {
            return shoot();
        } else {
            sort();
            new Delay(0.5);
        }
        return shoot();
    }

    public Command sort() {
        return new ParallelGroup(
                indexer.autoSet()
        );
    }

    public Command intake() {
        return new SequentialGroup(
                intake.spinIntake(),
                indexer.spinIndexer()
        );
    }

    public Command intakeStop() {
        return new SequentialGroup(
                intake.stopIntake(),
                indexer.stopIndexer()
        );
    }

    public Command autonomousRoutine() {
        return new ParallelGroup(
                flywheel.shootFlywheel(),
                new SequentialGroup(
                        sort(),
                        new Delay(Beginning),
                        safeShoot(),
                        new Delay(shootFarDelay),
                        new FollowPath(paths.Path1),
                        intake(),
                        new FollowPath(paths.Path2),
                        new Delay(intakeDelay),
                        sort(),
                        new FollowPath(paths.Path3),
                        intakeStop(),
                        safeShoot(),
                        new Delay(shootFarDelay),
                        new FollowPath(paths.Path4),
                        intake(),
                        new FollowPath(paths.Path5),
                        new Delay(intakeDelay),
                        sort(),
                        new FollowPath(paths.Path6),
                        intakeStop(),
                        safeShoot(),
                        new Delay(shootFarDelay)
                )
        );
    }



//        switch (pathState) {
//            case 0:
//                flywheel.shootFlywheelFar().schedule();
//                new ParallelGroup(
//                        feeder.setArmDown(),
//                        feeder.turnWheelsOn(),
//                        indexer.spinIndexer()).schedule();
//                new Delay(delay1);
//                setPathState(1);
//                return 0;
//            case 1:
//                follower.followPath(paths.Path1);
//                intake.spinIntake();
//                setPathState(2);
//                return 1;
//            case 2:
//                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                if(!follower.isBusy()) {
//                    /* Score Preload */
//                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                    new Delay(delay2);
//                    intake.stopIntake();
//                    follower.followPath(paths.Path2, true);
//                    setPathState(3);
//                }
//                return 2;
//            case 3:
//                if(!follower.isBusy()) {
//                    new ParallelGroup(
//                            feeder.setArmDown(),
//                            feeder.turnWheelsOn(),
//                            indexer.spinIndexer()).schedule();
//                    new Delay(delay3);
//                    follower.followPath(paths.Path3);
//                    intake.spinIntake();
//                    setPathState(4);
//                }
//                return 3;
//            case 4:
//                if(!follower.isBusy()) {
//                    new Delay(delay4);
//                    follower.followPath(paths.Path4);
//                    intake.stopIntake();
//                    setPathState(5);
//                }
//                return 4;
//            case 5:
//                if(!follower.isBusy()) {
//                    new Delay(delay5);
//                    follower.followPath(paths.Path5, true);
//                    setPathState(6);
//                }
//                return 5;
//            case 6:
//                if(!follower.isBusy()) {
//                    new ParallelGroup(
//                            feeder.setArmDown(),
//                            feeder.turnWheelsOn(),
//                            indexer.spinIndexer()).schedule();
//                    setPathState(-1);
//                }
//                panelsTelemetry.debug("FINAL TIME", opmodeTimer);
//                return 6;
//        }
//        return -1;

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}
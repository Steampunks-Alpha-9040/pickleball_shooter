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

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierCurve;

import org.firstinspires.ftc.teamcode.util.pedropathing.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class PedroAuto extends BaseOpMode {

    public PedroAuto(){
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
        public PathChain Path7;
        public PathChain Path8;
        public PathChain Path9;

        public Paths(Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(42.000, 5.300), new Pose(12.000, 5.300))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(12.000, 5.300),
                                    new Pose(56.000, 5.000),
                                    new Pose(57.500, 16.500)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(57.500, 16.500),
                                    new Pose(58.000, 58.500),
                                    new Pose(23.500, 58.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(23.500, 58.000),
                                    new Pose(24.000, 67.000),
                                    new Pose(16.000, 68.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(16.000, 68.000),
                                    new Pose(54.000, 67.500),
                                    new Pose(55.000, 76.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(55.000, 76.000),
                                    new Pose(51.500, 84.000),
                                    new Pose(28.000, 82.500)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path7 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(28.000, 82.500), new Pose(48.000, 90.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path8 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(48.000, 90.000),
                                    new Pose(91.000, 37.000),
                                    new Pose(22.000, 32.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            Path9 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(22.000, 32.000), new Pose(57.500, 16.500))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();
        }

    }

    double delay1 = 1.0;
    double delay2 = 1.0;
    double delay3 = 1.0;
    double delay4 = 1.0;
    double delay5 = 2.0; //Gate delay, very important
    double delay6 = 1.0;
    double delay7 = 1.0;
    double delay8 = 1.0;
    double delay9 = 1.0;

    public Command shoot() {
        return new ParallelGroup(
                feeder.setArmDown(),
                feeder.turnWheelsOn()
        );
    }

    public Command autonomousRoutine() {
        return new ParallelGroup(
                flywheel.shootFlywheel(),
                new SequentialGroup(
                        new Delay(delay1),
                        shoot(),
                        new Delay(delay2),
                        intake.spinIntake(),
                        new FollowPath(paths.Path1),
                        new Delay(delay3),
                        intake.stopIntake(),
                        new FollowPath(paths.Path2),
                        shoot(),
                        new Delay(delay4),
                        intake.spinIntake(),
                        new FollowPath(paths.Path3),
                        new Delay(delay5),
                        new FollowPath(paths.Path4),
                        new Delay(delay6),
                        new FollowPath(paths.Path5),
                        shoot(),
                        new Delay(delay7),
                        new FollowPath(paths.Path6),
                        new Delay(delay8),
                        new FollowPath(paths.Path7),
                        shoot(),
                        new FollowPath(paths.Path8),
                        new Delay(delay9),
                        new FollowPath(paths.Path9)
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
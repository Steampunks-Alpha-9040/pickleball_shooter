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
public class BetterMazdaMiataAuto extends BaseOpMode {

//    public BetterMazdaMiataAuto(){
//        addComponents(
//                new SubsystemComponent(
//                        super.drivebase,
//                        super.indexer,
//                        super.feeder,
//                        super.flywheel
//                ),
//                new PedroComponent(Constants::createFollower),
//                BulkReadComponent.INSTANCE,
//                BindingsComponent.INSTANCE
//
//        );
//    }
//
//    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
//    public Follower follower; // Pedro Pathing follower instance
//    private int pathState; // Current autonomous path state (state machine)
//    private Paths paths; // Paths defined in the Paths class
//    private Timer pathTimer, opmodeTimer;
//
//    @Override
//    public void onInit() {
//        pathTimer = new Timer();
//        opmodeTimer = new Timer();
//        opmodeTimer.resetTimer();
//        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
//
//        follower = Constants.createFollower(hardwareMap);
//        paths = new Paths(follower);
//        follower.setStartingPose(new Pose(64.145, 8.79, Math.toRadians(180)));
//
//        // Build paths
//
//        panelsTelemetry.debug("Status", "Initialized");
//        panelsTelemetry.update(telemetry);
//    }
//
//    @Override
//    public void onStartButtonPressed() {
//        follower.update(); // Update Pedro Pathing
//        //pathState = autonomousPathUpdate(); // Update autonomous state machine
//
//        autonomousRoutine().schedule();
//        // Log values to Panels and Driver Station
//        panelsTelemetry.debug("Path State", pathState);
//        panelsTelemetry.debug("X", follower.getPose().getX());
//        panelsTelemetry.debug("Y", follower.getPose().getY());
//        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
//        panelsTelemetry.update(telemetry);
//    }
//
//    public static class Paths {
//
//        public PathChain IntakeHuman;
//        public PathChain ShootFirst;
//        public PathChain IntakeMiddleAndOpenGate;
//        public PathChain ShootSecond;
//        public PathChain IntakeClose;
//        public PathChain ShootThird;
//        public PathChain IntakeGate;
//        public PathChain ShootLast;
//        public PathChain LeaveShootingZone;
//
//        public Paths(Follower follower) {
//            IntakeHuman = follower
//                    .pathBuilder()
//                    .addPath(new BezierLine(new Pose(64.145, 8.790), new Pose(12.000, 9.000)))
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            ShootFirst = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(12.000, 9.000), new Pose(48.000, 10.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            IntakeMiddleAndOpenGate = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierCurve(
//                                    new Pose(48.000, 10.000),
//                                    new Pose(50.000, 63.000),
//                                    new Pose(49.000, 60.000),
//                                    new Pose(14.000, 61.000)
//                            )
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            ShootSecond = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierCurve(
//                                    new Pose(14.000, 61.000),
//                                    new Pose(53.000, 54.000),
//                                    new Pose(64.145, 29.000)
//                            )
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            IntakeClose = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierCurve(
//                                    new Pose(64.145, 29.000),
//                                    new Pose(44.000, 38.000),
//                                    new Pose(18.000, 36.000)
//                            )
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            ShootThird = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(18.000, 36.000), new Pose(64.145, 29.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            IntakeGate = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(64.145, 29.000), new Pose(14.000, 24.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            ShootLast = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(14.000, 24.000), new Pose(64.145, 29.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//
//            LeaveShootingZone = follower
//                    .pathBuilder()
//                    .addPath(
//                            new BezierLine(new Pose(64.145, 29.000), new Pose(64.145, 35.000))
//                    )
//                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
//                    .build();
//        }
//    }
//
//    double shootFarDelay = 1.0;
//    double Beginning = 1.0;
//    double intakeDelay = 1.0;
//
//    public Command shoot() {
//        return new ParallelGroup(
//                feeder.setArmDown(),
//                feeder.turnWheelsOn()
//        );
//    }
//
////    public Command safeShoot() {
////        if (indexer.checkValid()) {
////            return shoot();
////        } else {
////            sort();
////            new Delay(0.5);
////        }
////        return shoot();
////    }
//
//    public Command stopShoot() {
//        return new ParallelGroup(
//                feeder.setArmUp(),
//                feeder.turnWheelsOff()
//        );
//    }
//
//    public Command sort() {
//        return new ParallelGroup(
//                indexer.autoSet()
//        );
//    }
//
//    public Command intake() {
//        return new SequentialGroup(
//                intake.spinIntake(),
//                indexer.spinIndexer()
//        );
//    }
//
//    public Command intakeStop() {
//        return new SequentialGroup(
//                intake.stopIntake(),
//                indexer.stopIndexer()
//        );
//    }
//
//    public Command autonomousRoutine() {
//        return new ParallelGroup(
//                flywheel.shootFlywheel(),
//                new SequentialGroup(
//                        sort(),
//                        new Delay(Beginning),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeHuman),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.ShootFirst),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeMiddleAndOpenGate),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.ShootSecond),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeClose),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.ShootThird),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeGate),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.ShootLast),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        new FollowPath(paths.LeaveShootingZone)
//                )
//        );
//    }

}
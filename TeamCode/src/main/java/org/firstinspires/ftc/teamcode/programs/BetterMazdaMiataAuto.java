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

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.util.Drawing;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;

@Autonomous(name = "BetterMazda", group = "Autonomous")
@Configurable // Panels
public class BetterMazdaMiataAuto extends BaseOpMode {

    public BetterMazdaMiataAuto(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase,
//                        super.indexer,
//                        super.feeder,
                        super.flywheel,
                        super.turret
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

        follower = PedroComponent.follower();
        paths = new Paths(follower);


        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
        Drivebase.INSTANCE.zeroTurretQuadature();
        Drivebase.INSTANCE.zeroHoodQuadature();
    }


    @Override
    public void onStartButtonPressed() {

        autonomousRoutine().schedule();
        follower.setStartingPose(new Pose(64.145, 8.79, Math.toRadians(180)));
//        Drivebase.INSTANCE.setStartingPose(new Pose(64.145, 8.79, Math.toRadians(90)));
        // Log values to Panels and Driver Station


    }

    @Override
    public void onUpdate(){
        follower.update(); // Update Pedro Pathing

        Drawing.drawDebug(follower);

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {

        public PathChain testingAuto;
        public PathChain PathIntakeClose;
        public PathChain PathShootFirst;
        public PathChain PathIntakeMiddle;
        public PathChain PathShootSecond;

        public Paths(Follower follower) {
            PathIntakeClose = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(64.145, 8.790),
                                    new Pose(51.908, 39.279),
                                    new Pose(18.000, 36.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            PathShootFirst = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(18.000, 36.000), new Pose(48.000, 10.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            PathIntakeMiddle = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(48.000, 10.000),
                                    new Pose(49.919, 64.600),
                                    new Pose(43.227, 61.345),
                                    new Pose(18.000, 60.000)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();

            PathShootSecond = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(18.000, 60.000), new Pose(48.000, 10.000))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                    .build();
        }
    }


    double shootFarDelay = 1.0;
    double Beginning = 1.0;
    double intakeDelay = 1.0;

//    public Command shoot() {
//        return new ParallelGroup(
//                feeder.setArmDown(),
//                feeder.turnWheelsOn()
//        );
//    }
//
//    public Command safeShoot() {
//        if (indexer.checkValid()) {
//            return shoot();
//        } else {
//            sort();
//            new Delay(0.5);
//        }
//        return shoot();
//    }
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

    public Command autonomousRoutine() {
        return new ParallelGroup(
//                flywheel.shootFlywheel(),
                new SequentialGroup(
//                        sort(),
                        new Delay(Beginning),
                        new FollowPath(paths.PathIntakeClose),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
                        new FollowPath(paths.PathIntakeClose),
                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
                        new FollowPath(paths.PathShootFirst),
//                        safeShoot(),
                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
                        new FollowPath(paths.PathIntakeMiddle),
                        new Delay(intakeDelay),
//                        sort(),
                        new FollowPath(paths.PathShootSecond),
//                        intakeStop(),
//                        safeShoot(),
                        new Delay(shootFarDelay)
//                        stopShoot()
                        )
        );
    }

}
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

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;

@Autonomous(name = "Testing Auto", group = "Autonomous")
@Configurable // Panels
public class PleaseWorkAuto extends BaseOpMode {

    public PleaseWorkAuto(){
        addComponents(
                new SubsystemComponent(
                        super.drivebase,
                        super.indexer,
                        super.feeder,
                        super.flywheel
                ),
                new PedroComponent(org.firstinspires.ftc.teamcode.util.pedropathing.Constants::createFollower),
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


        turret.setSide(Constants.Side.BLUE);

        drivebase.setFollower(PedroComponent.follower());
        drivebase.getFollower().setStartingPose(new Pose(63.7, 8.69, Math.toRadians(180)));

        paths = new Paths(drivebase.getFollower());


        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onStartButtonPressed() {
        drivebase.getFollower().update(); // Update Pedro Pathing
        //pathState = autonomousPathUpdate(); // Update autonomous state machine

        autonomousRoutine().schedule();
        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void onUpdate(){
        telemetry.update();
    }


    public static class Paths {
        public PathChain FirstIntake;
        public PathChain ShootFirst;
        public PathChain SecondIntake;
        public PathChain SecondShoot;

        public Paths(Follower follower) {
            FirstIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(63.700, 8.690),
                                    new Pose(51.908, 39.279),
                                    new Pose(14.000, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            ShootFirst = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(14.000, 36.000),

                                    new Pose(48.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(100))

                    .build();

            SecondIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(48.000, 11.000),
                                    new Pose(54.247, 64.994),
                                    new Pose(45.587, 54.656),
                                    new Pose(14.000, 58.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(100), Math.toRadians(180))

                    .build();

            SecondShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(14.000, 58.000),

                                    new Pose(48.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(100))

                    .build();
        }
    }




    public Command shoot() {
        return new ParallelGroup(
                feeder.setArmDown(),
                feeder.turnWheelsOn()
        );
    }

    public Command safeShoot() {
//        if (indexer.checkValid()) {
//            return shoot();
//        } else {
//            sort();
//            new Delay(0.5);
//        }
        return shoot();
    }

    public Command stopShoot() {
        return new ParallelGroup(
                feeder.setArmUp(),
                feeder.turnWheelsOff()
        );
    }

//    public Command sort() {
//        return new ParallelGroup(
//                indexer.autoSet()
//        );
//    }

    public Command intake() {
        return new SequentialGroup(
                intake.spinIntake()

        );
    }

    public Command intakeStop() {
        return new SequentialGroup(
                intake.stopIntake()
        );
    }

    public Command spinIndexer() {
        return new SequentialGroup(
                indexer.spinIndexer()
        );
    }

    public Command zeroEverything(){
        return new ParallelGroup(
                turret.moveTurretZero(),
                flywheel.spinHoodZero(),
                flywheel.stopFlywheel()
        );
    }

    double shootFarDelay = 5.0;
    double Beginning = 1.0;
    double intakeDelay = 1.0;

    public Command autonomousRoutine() {
        return new ParallelGroup(
                flywheel.shootFlywheel(),
                new SequentialGroup(
//                        stopShoot(),
//                        sort(),
                        new Delay(Beginning),
//                        safeShoot(),
                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        spinIndexer(),
                        new FollowPath(paths.FirstIntake),
                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
                        new FollowPath(paths.ShootFirst),
//                        safeShoot(),
                        new Delay(shootFarDelay),
//                        stopShoot(),
                        turret.setHomeTrue(),
//                        stopShoot(),
//                        intake(),
                        new FollowPath(paths.SecondIntake)
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.SecondShoot),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot()
//                        intake(),
//                        new FollowPath(paths.IntakeClose),
//                        new Delay(intakeDelay),
//                        intakeStop(),
////                        sort(),
//                        new FollowPath(paths.ShootThird),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeGate),
//                        new Delay(intakeDelay),
//                        intakeStop(),
////                        sort(),
//                        new FollowPath(paths.ShootLast),
//                        safeShoot(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        new FollowPath(paths.LeaveShootingZone)
//                        zeroEverything()
                )
        );
    }

}
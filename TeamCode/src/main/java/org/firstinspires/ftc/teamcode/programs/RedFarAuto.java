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

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;

@Autonomous(name = "Far Red", group = "Autonomous")
@Configurable // Panels
public class RedFarAuto extends BaseOpMode {

    public RedFarAuto(){
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


        turret.setSide(Constants.Side.RED);
        flywheel.setSide(Constants.Side.RED);
        Constants.OpModeConstants.setSide(Constants.Side.RED);


        drivebase.setFollower(PedroComponent.follower());
        drivebase.getFollower().setStartingPose(new Pose(79.510, 8.993, Math.toRadians(0)));
        drivebase.INSTANCE.getBR().setCurrentPosition(0);
        drivebase.INSTANCE.getFL().setCurrentPosition(0);

        paths = new Paths(drivebase.getFollower());


        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        drivebase.zeroTurretQuadature();
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
        public PathChain FirstShoot;
        public PathChain SecondIntake;
        public PathChain SecondShoot;
        public PathChain GoToWall;

        public Paths(Follower follower) {
            FirstIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(79.510, 8.993),
                                    new Pose(92.092, 39.279),
                                    new Pose(131.000, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            FirstShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(131.000, 36.000),

                                    new Pose(96.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(80))

                    .build();

            SecondIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(96.000, 11.000),
                                    new Pose(85.228, 43.551),
                                    new Pose(92.118, 63.705),
                                    new Pose(131.000, 55.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(80), Math.toRadians(0))

                    .build();

            SecondShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(131.000, 55.500),

                                    new Pose(96.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(80))

                    .build();

            GoToWall = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(96.000, 11.000),

                                    new Pose(80.693, 7.811)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(80), Math.toRadians(90))

                    .build();
        }
    }


    public Command transfer() {
        return new ParallelGroup(
                feeder.setArmDown(),
                feeder.turnWheelsOn()
        );
    }

    public Command safeTransfer() {
//        if (indexer.checkValid()) {
//            return transfer();
//        } else {
//            sort();
//            new Delay(0.5);
//        }
        return transfer();
    }

    public Command stopTransfer() {
        return new ParallelGroup(
                //Removed until intake is improved
//                feeder.setArmUp(),
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

    public Command spinIndexerSlow() {
        return new SequentialGroup(
                indexer.spinIndexerSlow()
        );
    }

    public Command spinIndexerFast() {
        return new SequentialGroup(
                indexer.spinIndexerFast()
        );
    }

    public Command stopIndexer(){
        return new SequentialGroup(
                indexer.stopIndexer()
        );
    }

    public Command zeroEverything(){
        return new ParallelGroup(
                turret.moveTurretZero(),
                flywheel.spinHoodZero(),
                flywheel.stopFlywheel()
        );
    }

    public Command zeroTurret(){
        return new SequentialGroup(
                turret.setTurretQuadCommand(0)
        );
    }

    double shootFarDelay = 3.7;
    double Beginning = 1.5;
    double intakeDelay = 0.5;
    double testingDelay = 1;

    public Command autonomousRoutine() {
        return new ParallelGroup(
                new SequentialGroup(
                        flywheel.changeBool(),
                        stopTransfer(),
//                        sort(),
                        intakeStop(),
                        new Delay(Beginning),
                        transfer(),
                        new Delay(intakeDelay),
                        spinIndexerSlow(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        intake(),
                        spinIndexerFast(),
                        new FollowPath(paths.FirstIntake),
                        new Delay(intakeDelay),
                        spinIndexerSlow(),
                        intakeStop(),
//                        sort(),
                        new FollowPath(paths.FirstShoot),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        intake(),
                        spinIndexerFast(),
                        new FollowPath(paths.SecondIntake),
                        new Delay(intakeDelay),
                        intakeStop(),
                        spinIndexerSlow(),
//                        sort(),
                        new FollowPath(paths.SecondShoot),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        new FollowPath((paths.GoToWall)),
                        flywheel.stopFlywheel(),
                        turret.setHomeTrue()
//                        intake(),
//                        spinIndexerFast()
//                        new FollowPath(paths.IntakeClose),
//                        new Delay(intakeDelay),
//                        intakeStop(),
////                        sort(),
//                        new FollowPath(paths.ShootThird),
//                        safeTransfer(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeGate),
//                        new Delay(intakeDelay),
//                        intakeStop(),
////                        sort(),
//                        new FollowPath(paths.ShootLast),
//                        safeTransfer(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        new FollowPath(paths.LeaveShootingZone)
//                        zeroEverything()
                )
        );
    }

}
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

@Autonomous(name = "Close Blue", group = "Autonomous")
@Configurable // Panels
public class BlueCloseAuto extends BaseOpMode {

    public BlueCloseAuto(){
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
        flywheel.setSide(Constants.Side.BLUE);
        Constants.OpModeConstants.setSide(Constants.Side.BLUE);

        drivebase.setFollower(PedroComponent.follower());
        drivebase.getFollower().setStartingPose(new Pose(26.426, 131.213, Math.toRadians(144)));
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
        public PathChain ShootFirst;
        public PathChain IntakeFar;
        public PathChain ShootSecond;
        public PathChain IntakeMiddle;
        public PathChain ShootThird;
        public PathChain GoToWall;

        public Paths(Follower follower) {
            ShootFirst = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(26.426, 131.213),

                                    new Pose(60.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(180))

                    .build();

            IntakeFar = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 84.000),

                                    new Pose(14.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            ShootSecond = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(14.000, 84.000),

                                    new Pose(60.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            IntakeMiddle = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(60.000, 84.000),
                                    new Pose(55.279, 53.902),
                                    new Pose(54.541, 71.025),
                                    new Pose(12.000, 53.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            ShootThird = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(12.000, 53.000),

                                    new Pose(60.000, 72.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            GoToWall = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 72.000),

                                    new Pose(56.693, 136.190)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-90))

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
                        new FollowPath(paths.ShootFirst),
                        new Delay(Beginning),
                        transfer(),
                        new Delay(intakeDelay),
                        spinIndexerSlow(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        intake(),
                        spinIndexerFast(),
                        new FollowPath(paths.IntakeFar),
                        new Delay(intakeDelay),
                        spinIndexerSlow(),
                        intakeStop(),
//                        sort(),
                        new FollowPath(paths.ShootSecond),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        intake(),
                        spinIndexerFast(),
                        new FollowPath(paths.IntakeMiddle),
                        new Delay(intakeDelay),
                        intakeStop(),
                        spinIndexerSlow(),
//                        sort(),
                        new FollowPath(paths.ShootThird),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        new FollowPath((paths.GoToWall)),
                        flywheel.changeBool(),
                        turret.setHomeTrue()
//                        intake(),
//                        spinIndexerFast()
//                        new FollowPath(paths.IntakeClose),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
//                        new FollowPath(paths.ShootThird),
//                        safeTransfer(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        intake(),
//                        new FollowPath(paths.IntakeGate),
//                        new Delay(intakeDelay),
//                        intakeStop(),
//                        sort(),
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
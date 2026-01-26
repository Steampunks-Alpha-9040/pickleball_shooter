package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import dev.nextftc.core.commands.utility.InstantCommand;
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

@Autonomous(name = "Far Blue", group = "Autonomous")
@Configurable // Panels
public class BlueFarAuto extends BaseOpMode {

    public BlueFarAuto(){
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
        drivebase.getFollower().setStartingPose(new Pose(64.490, 8.993, Math.toRadians(180)));
        drivebase.INSTANCE.getBR().setCurrentPosition(0);
        drivebase.INSTANCE.getFL().setCurrentPosition(0);

        paths = new Paths(drivebase.getFollower());


        // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        drivebase.zeroHoodQuadature();
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
        public PathChain GoOut;

        public Paths(Follower follower) {
            FirstIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(64.490, 8.993),
                                    new Pose(51.908, 39.279),
                                    new Pose(13.000, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            FirstShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(13.000, 36.000),

                                    new Pose(48.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(100))

                    .build();

            SecondIntake = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(48.000, 11.000),
                                    new Pose(58.772, 43.551),
                                    new Pose(51.882, 63.705),
                                    new Pose(13.000, 55.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(100), Math.toRadians(180))

                    .build();

            SecondShoot = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(13.000, 55.500),

                                    new Pose(48.000, 11.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(100))

                    .build();

            GoOut = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(48.000, 11.000),

                                    new Pose(48.000, 25.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(100), Math.toRadians(90))

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

    public void setAutoToTeleOpPose(Pose pose){
        Constants.AutoToTeleOpValues.pose = pose;
    }

    public void setAutoToTeleOpTurret(double turretAngle){
        Constants.AutoToTeleOpValues.turretAngle = turretAngle;
    }

    public void setAutoToTeleOpHood(double hoodAngle){
        Constants.AutoToTeleOpValues.hoodAngle = hoodAngle;
    }

    public void setAutoToTeleOpIndexer(double indexerPose){
        Constants.AutoToTeleOpValues.indexerPosition = indexerPose;
    }

    public Command storeValues() {
        return new InstantCommand(() -> {
            setAutoToTeleOpPose(Drivebase.INSTANCE.getFollower().getPose());
            setAutoToTeleOpTurret(Drivebase.INSTANCE.getTurretQuadature());
            setAutoToTeleOpHood(Drivebase.INSTANCE.getHoodQuadature());
//                setAutoToTeleOpIndexer();
        }
        );
    }

    public Command setHomeTrue() {
        return new InstantCommand(
                () -> {
                    turret.sethome(true);
                }
        );
    }

    double shootFarDelay = 3.3;
    double Beginning = 1.5;
    double intakeDelay = 0.5;

    double testingDelay = 1;
    double testing = 0.5;

    public Command autonomousRoutine() {
        return new ParallelGroup(
                new SequentialGroup(
                        flywheel.changeBool(),
                        stopTransfer(),
                        intake(),
//                        sort(),
                        new Delay(Beginning),
                        spinIndexerSlow(),
                        new Delay(intakeDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        storeValues(),
                        stopTransfer(),
                        stopIndexer(),
                        new Delay(testing),
                        spinIndexerFast(),
                        new FollowPath(paths.FirstIntake),
                        new Delay(intakeDelay),
                        stopIndexer(),
                        new Delay(testing),
                        spinIndexerSlow(),
//                        sort(),
                        new FollowPath(paths.FirstShoot),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        storeValues(),
                        stopTransfer(),
                        stopIndexer(),
                        new Delay(testing),
                        spinIndexerFast(),
                        new FollowPath(paths.SecondIntake),
                        new Delay(intakeDelay),
                        stopIndexer(),
                        new Delay(testing),
                        spinIndexerSlow(),
//                        sort(),
                        new FollowPath(paths.SecondShoot),
                        new Delay(testingDelay),
                        transfer(),
                        new Delay(shootFarDelay),
                        stopTransfer(),
                        storeValues(),
                        new FollowPath((paths.GoOut)),
                        flywheel.changeBool(),
                        setHomeTrue(),
                        storeValues()
//                        intake(),
//                        spinIndexerFast()
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
//                        safeTransfer(),
//                        new Delay(shootFarDelay),
//                        stopShoot(),
//                        new FollowPath(paths.LeaveShootingZone)
//                        zeroEverything()
                )
        );
    }

}
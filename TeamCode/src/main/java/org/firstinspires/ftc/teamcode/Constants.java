package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.geometry.Vector2d;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.util.Util;

@Configurable
public class Constants {

    public enum Side{
        BLUE, RED
    }
    public static double downPosition = 0.92;

    public static final double robotWidth = 17.386;
    public static final double robotLength = 15.621;


    public static class OpModeConstants{
        public static Side side;
        public static Vector2d BLUEscore = new Vector2d(8, 136);
        public static Vector2d REDscore = new Vector2d(136, 136);

        public static void setSide(Constants.Side gameSide){
            side = gameSide;
        }
    }
    public static class DrivebaseConstants {
        public static final String FR = "fr";
        public static final String FL = "fl";
        public static final String BR = "br";
        public static final String BL = "bl";
        public static final String IMU = "pinpoint";

        public static final double slowScalar = 0.5;

//0.9877
        public static final double yawScalar = 1;
        public static final double constantSigmaOdo = 0.1; //todo: tune this value.

        public static Pose autoEndPos = new Pose();

    }

    public static class FlywheelConstants{
        public static final String flywheelName = "flywheel";
        public static final String hoodName = "hood";

        public static final Util.GoBILDA flywheelMotor = Util.GoBILDA.BARE;

        public static final double flywheelVelocityTolerance = 2.0; //10 rpm tolerance is probably good enough

        public static final double flywheel_kP = 0.006;
        public static final double flywheel_kI = 0.0;
        public static final double flywheel_kD = 0.0;
        public static final double flywheel_kF = 0;

        public static double hoodPositionToleranceRAD = 0.05;
        public static double hood_kP = 0.9;
        public static double hood_kI = 0.0;
        public static double hood_kD = 0.55;
        public static double hood_kF = 0.0;

        public static final double[][] flywheelVals = {
                {72,17, 5000, 4.7}, //far zone
                {0,0, 3000, 0.9} //close zone

        };


    }

    public static class TurretConstants {
        public static final String turretMasterName = "turretM";
        public static final String turretSlaveName = "turretS";
        public static double turret_kP = 1; //The like main pushing force, the constant :)
        public static double turret_kI = 0.005; //The more you aren't making it to the setpoint, the more you increase this
        public static double turret_kD = 0.5; //slows down/speeds up the closer/farther away u are
        public static double turret_kF = 0.017; //friction, tune so when the turret barely moves
        public static double turretTolerance_VisionAngleRad = 0.001;

        public static final double encoderToTurret = 145.0/60.0;

    }

    public static class FeederConstants{
        public static final String feederArm = "feederArm";
        public static final String feeder = "feeder";

        public static final double servoToArm = 48.0 / 30.0;

        public static final double feederArm_kP = 10.0; //The like main pushing force, the constant :)
        public static final double feederArm_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double feederArm_kD = 0.0; //slows down/speeds up the closer/farther away u are
        public static final double feederArm_kF = 0.0; //friction, tune so when the turret barely moves

        public static final double feederArmTolerance = 3.0;

    }

    public static class IndexerConstants{
        public static final String indexer = "indexer";
        public static final String feederSensor = "feederSensor";
        public static final String rampRightSensor = "rampRightSensor";
        public static final String rampLeftSensor = "rampLeftSensor";
        public static final double motorToIndexer = 0.0;

        public static final double indexer_kP = 0.007; //The like main pushing force, the constant :)
        public static final double indexer_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double indexer_kD = 0.0003; //slows down/speeds up the closer/farther away u are
        public static final double indexer_kF = 0.05; //friction, tune so when the turret barely moves

        public static final double indexer_DutyCycle = 0.4;

        public static final double indexerTolerance = 3.0;

    }


    public static class IntakeConstants {
        // PID coefficients for NextFTC velocity control
        public static final double intake_kP = 2;  // tweak as needed
        public static final double intake_kI = 0.0;
        public static final double intake_kD = 0.0;
        public static final double intake_kF = 0.05; // feedforward, tweak if needed

        // target speed for intake (can be 0.5 or whatever works for your motor)
        public static final double intakeSpeed = 0.4;

        // name of the motor in the hardware map
        public static final String intakeName = "intake";
    }

    public static class VisionConstants{
        public static final String limelight = "limelight";
        public static final double accepted_y_offset = 0.1;
        public static final double accepted_pipeline_latency_ms = 200;
        public static final double kTagAreaThresholdForYawCheck = 2.0;
        public static final double kDefaultYawDiffThreshold = 5.0;
        public static final double tagTargetTolerance = 0.5;

        public static final Vector2d turretCenterToRobotCenter = new Vector2d(-(78.66)/1000,0);
        public static final Vector2d cameraToTurretCenter = new Vector2d(-(37.448)/1000, (109.226)/1000);


    }

    public static class RelativeShootingConstants{

        public static final double airTime = 1.0;
        public static final double velocityTolerance = 500000.0;
        public static boolean allowShootOnMove = true;
        public static final double maxTurretAdjustment = 45.0;

        public static double flywheelRPM = 0.0;
        public static double hoodAngle = 3;
        
        public static final double turretOffset = 3.097;
    }

}

package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.util.Util;

public class Constants {

    public enum Side{
        BLUE, RED
    }
    public static class OpModeConstants{
        public static Side side = Side.BLUE;
        public static Vector2d BLUEscore = new Vector2d(10, 150);
        public static Vector2d REDscore = new Vector2d(144, 144);
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

        public static final Pose2D startingPose = new Pose2D(DistanceUnit.INCH, 72, 0, AngleUnit.DEGREES, 0);

    }

    public static class FlywheelConstants{
        public static final String flywheelName = "flywheel";
        public static final String hoodName = "hood";

        public static final Util.GoBILDA flywheelMotor = Util.GoBILDA.BARE;

        public static final double flywheelVelocityTolerance = 10.0; //10 rpm tolerance is probably good enough

        public static final double flywheel_kP = 2.0;
        public static final double flywheel_kI = 0.0;
        public static final double flywheel_kD = 0.5;
        public static final double flywheel_kF = 0.0;

        public static final double hoodPositionToleranceRAD = 0.01;

        public static final double hood_kP = 7.0;
        public static final double hood_kI = 0.0;
        public static final double hood_kD = 0.0;
        public static final double hood_kF = 0.0;

        public static final double hoodStartingPos = (0.53756141+0.51696652)/2;

    }

    public static class TurretConstants {
        public static final String turretMasterName = "turretM";
        public static final String turretSlaveName = "turretS";
        public static final double turret_kP = 2; //The like main pushing force, the constant :)
        public static final double turret_kI = 0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double turret_kD = 0.5; //slows down/speeds up the closer/farther away u are
        public static final double turret_kF = 0; //friction, tune so when the turret barely moves


        public static final double turretTolerance_VisionAngleRad = 0.02;

        public static final double encoderToTurret = 145.0/60.0;

    }

    public static class FeederConstants{
        public static final String feederArm = "feederArm";
        public static final String feederWheel = "feederWheel";

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

        public static final double indexer_kP = 10.0; //The like main pushing force, the constant :)
        public static final double indexer_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double indexer_kD = 0.0; //slows down/speeds up the closer/farther away u are
        public static final double indexer_kF = 0.0; //friction, tune so when the turret barely moves

        public static final double indexer_DutyCycle = 0.3;

        public static final double indexerTolerance = 3.0;

    }


    public static class IntakeConstants {
        // PID coefficients for NextFTC velocity control
        public static final double intake_kP = 2;  // tweak as needed
        public static final double intake_kI = 0.0;
        public static final double intake_kD = 0.0;
        public static final double intake_kF = 0.05; // feedforward, tweak if needed

        // target speed for intake (can be 0.5 or whatever works for your motor)
        public static final double intakeSpeed = 0.6;

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


}

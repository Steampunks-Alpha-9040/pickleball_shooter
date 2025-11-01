package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.util.Util;

public class Constants {


    public static class DrivebaseConstants {
        public static final String FR = "fr";
        public static final String FL = "fl";
        public static final String BR = "br";
        public static final String BL = "bl";
        public static final String IMU = "imu";

        public static final double slowScalar = 0.5;

    }

    public static class FlywheelConstants{
        public static final String flywheelName = "flywheel";
        public static final String hoodName = "hood";

        public static final Util.GoBILDA flywheelMotor = Util.GoBILDA.BARE;

        public static final double flywheelVelocityTolerance = 10.0; //10 rpm tolerance is probably good enough

        public static final double flywheel_kP = 2.0;
        public static final double flywheel_kI = 0.0;
        public static final double flywheel_kD = 0.0;
        public static final double flywheel_kF = 0.0;

        public static final double hood_kP = 10.0;
        public static final double hood_kI = 0.0;
        public static final double hood_kD = 0.0;
        public static final double hood_kF = 0.0;

    }

    public static class TurretConstants {
        public static final String turretMasterName = "turretM";
        public static final String turretSlaveName = "turrentS";
        public static final String turretEncoder = "turretEncoder";

        public static final double servoToTurret = 10.0 / 29.0;

        public static final double turret_kP = 10.0; //The like main pushing force, the constant :)
        public static final double turret_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double turret_kD = 0.0; //slows down/speeds up the closer/farther away u are
        public static final double turret_kF = 0.0; //friction, tune so when the turret barely moves


        public static final double turretTolerance = 3.0;

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

        public static final double indexer_DutyCycle = 0.35;

        public static final double indexerTolerance = 3.0;

    }


    public static class IntakeConstants {
        // PID coefficients for NextFTC velocity control
        public static final double intake_kP = 0.1;  // tweak as needed
        public static final double intake_kI = 0.0;
        public static final double intake_kD = 0.0;
        public static final double intake_kF = 0.05; // feedforward, tweak if needed

        // target speed for intake (can be 0.5 or whatever works for your motor)
        public static final double intakeSpeed = 0.5;

        // name of the motor in the hardware map
        public static final String intakeName = "intake";
    }

}

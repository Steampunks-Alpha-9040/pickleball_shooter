package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.controller.PIDFController;

public class Constants {

    public static class FlywheelConstants{
        public static final String hoodName = "hood";
        public static final String flywheelName = "flywheel";
        public static final String hoodEncoderName = "hoodEncoder";
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
        public static final String feederBottomOmni = "feederBot";
        public static final String feederTopWheel = "feederTop";
        public static final String feederArmEncoder = "feederArmEncoder";

        public static final double servoToArm = 48.0 / 30.0;

        public static final double feederArm_kP = 10.0; //The like main pushing force, the constant :)
        public static final double feederArm_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double feederArm_kD = 0.0; //slows down/speeds up the closer/farther away u are
        public static final double feederArm_kF = 0.0; //friction, tune so when the turret barely moves

        public static final double feederArmTolerance = 3.0;

    }

    public static class IndexerConstants{
        public static final String indexer = "indexer";
        public static final String armColor = "armColor";

        public static final double motorToIndexer = 0.0;

        public static final double indexer_kP = 10.0; //The like main pushing force, the constant :)
        public static final double indexer_kI = 0.0; //The more you aren't making it to the setpoint, the more you increase this
        public static final double indexer_kD = 0.0; //slows down/speeds up the closer/farther away u are
        public static final double indexer_kF = 0.0; //friction, tune so when the turret barely moves

        public static final double indexerTolerance = 3.0;

    }

    public static class Intake{
        public static final String intakeName = "intake";

        public static final double spinSpeed_DUTYCYCLE = 0.5;
    }



}

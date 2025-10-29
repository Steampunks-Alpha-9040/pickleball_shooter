package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.Util;

public class Constants {


    public static class DrivebaseConstants {
        public static final String FR = "fr";
        public static final String FL = "fl";
        public static final String BR = "br";
        public static final String BL = "bl";
        public static final String IMU = "imu";

        public static double xVelo = 81.34056;
        public static double yVelo = 65.43028;
        public static Vector frontLeftVector = new Vector(new Pose(xVelo, yVelo)).normalize();
        public static double maxPower = 1.0;
        public static DcMotorSimple.Direction FLdirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction BLdirection = DcMotorSimple.Direction.REVERSE;
        public static DcMotorSimple.Direction FRdirection = DcMotorSimple.Direction.FORWARD;
        public static DcMotorSimple.Direction BRdirection = DcMotorSimple.Direction.FORWARD;
        public static double motorUpdateThreshold = 0.01;
        public static boolean useBrakeModeInTeleOp = false;
        public static boolean useVoltageCompensation = false;
        public static double nominalVoltage = 12.0;
        public static double staticFrictionCoefficient = 0.1;

        public static final double slowScalar = 0.5;

    }

    public static class FlywheelConstants {
        public static final String flywheelName = "flywheel";

        public static final Util.GoBILDA flywheelMotor = Util.GoBILDA.BARE;

        public static final double flywheelVelocityTolerance = 10.0; //10 rpm tolerance is probably good enough

        public static final double kP = 10.0;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kF = 0.0;
    }

    public static class ImuConstants {
        public static String imuName = "imu";
        public static Pose podOffsets = new Pose(7, 0);
        public static DistanceUnit distanceUnit = DistanceUnit.INCH;
        public static GoBildaPinpointDriver.GoBildaOdometryPods podType = GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
        public static GoBildaPinpointDriver.EncoderDirection xEncoderDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
        public static GoBildaPinpointDriver.EncoderDirection yEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;

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

    public static class Intake{
        public static final String intakeName = "intake";

        public static final double spinSpeed_DUTYCYCLE = 0.5;
    }

}

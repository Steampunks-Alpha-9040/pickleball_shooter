package org.firstinspires.ftc.teamcode.util;

import com.arcrobotics.ftclib.hardware.motors.Motor;

public class Util {

    public enum GoBILDA {
        RPM_30(5264, 30), RPM_43(3892, 43), RPM_60(2786, 60), RPM_84(1993.6, 84),
        RPM_117(1425.2, 117), RPM_223(753.2, 223), RPM_312(537.6, 312), RPM_435(383.6, 435),
        RPM_1150(145.6, 1150), RPM_1620(103.6, 1620), BARE(28, 6000), NONE(0, 0);

        private double cpr, rpm;

        GoBILDA(double cpr, double rpm) {
            this.cpr = cpr;
            this.rpm = rpm;
        }

        public double getCPR() {
            return cpr;
        }

        public double getRPM() {
            return rpm;
        }

        public double getAchievableMaxTicksPerSecond() {
            return cpr * rpm / 60;
        }
    }


    /**
     *
     * @param current the current value
     * @param target the target value
     * @param tolerance the tolerance value
     * @return if current is within +tolerance or -tolerance of target
     */
    public static boolean isBetween(double current, double target, double tolerance){
        double difference = Math.abs(current - target);
        return tolerance > difference;
    }
}

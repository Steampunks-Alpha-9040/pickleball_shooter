package org.firstinspires.ftc.teamcode.util;

public class Util {


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

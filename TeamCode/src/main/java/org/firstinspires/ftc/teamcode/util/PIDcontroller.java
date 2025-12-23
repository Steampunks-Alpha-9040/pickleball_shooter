package org.firstinspires.ftc.teamcode.util;

public class PIDcontroller {

    private double kP, kI, kD, kF;
    private double setpoint;

    private double integral;
    private double previousError;

    private double outputMin = -1.0;
    private double outputMax = 1.0;

    private double tolerance = 0.026; // ~1.5 degrees in radians

    public PIDcontroller(double kP, double kI, double kD, double kF, double toleranceRadians) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(toleranceRadians);

    }

    // Forward = 0, wrap at ±π
    private double normalizeAngle(double angle) {
        angle = angle % (2.0 * Math.PI);
        if (angle > Math.PI) angle -= 2.0 * Math.PI;
        if (angle < -Math.PI) angle += 2.0 * Math.PI;
        return angle;
    }

    public void setSetpoint(double angleRadians) {
        this.setpoint = normalizeAngle(angleRadians);
    }

    public double calculate(double currentAngle) {
        currentAngle = normalizeAngle(currentAngle);

        double error = normalizeAngle(setpoint - currentAngle);

        // --- TOLERANCE CHECK ---
        if (Math.abs(error) <= tolerance) {
            integral = 0;
            previousError = error;
            return 0.0;
        }

        // Integral per loop
        integral += error;

        // Derivative per loop
        double derivative = error - previousError;

        // PIDF output
        double output =
                (kP * error) +
                        (kI * integral) +
                        (kD * derivative) +
                        kF;

        // Clamp
        if (output > outputMax) output = outputMax;
        if (output < outputMin) output = outputMin;

        previousError = error;

        return output;
    }

    public void reset() {
        integral = 0;
        previousError = 0;
    }
}

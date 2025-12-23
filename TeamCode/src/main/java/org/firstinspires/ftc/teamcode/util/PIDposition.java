package org.firstinspires.ftc.teamcode.util;

public class PIDposition {

    private double kP, kI, kD, kF;
    private double setpoint;

    private double integral;
    private double previousError;

    private double outputMin = -1.0;
    private double outputMax = 1.0;

    private double tolerance;

    public PIDposition(double kP, double kI, double kD, double kF, double toleranceRadians) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(toleranceRadians);
    }



    public void setSetpoint(double angleRadians) {
        this.setpoint = angleRadians;
    }

    public double calculate(double currentAngle) {
        double error = setpoint - currentAngle;

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

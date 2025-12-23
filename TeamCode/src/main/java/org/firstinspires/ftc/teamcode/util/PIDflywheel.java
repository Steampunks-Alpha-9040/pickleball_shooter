package org.firstinspires.ftc.teamcode.util;

public class PIDflywheel {

    private double kP, kI, kD, kF;
    private double setpoint;

    private double integral;
    private double previousError;

    private double outputMin = -1.0;
    private double outputMax = 1.0;

    private double tolerance; // ~1.5 degrees in radians

    public PIDflywheel(double kP, double kI, double kD, double kF, double toleranceRPM) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(toleranceRPM);
    }

    public void setSetpoint(double rpm) {
        this.setpoint = rpm;
    }


    public double calculate(double currentRPM) {
        double error = setpoint - currentRPM;

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

}

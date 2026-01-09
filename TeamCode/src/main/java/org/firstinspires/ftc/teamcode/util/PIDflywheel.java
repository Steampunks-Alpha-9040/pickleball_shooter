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

    public double getSetpoint(){
        return this.setpoint;
    }


    public double calculate(double currentRPM) {
        if (setpoint == 0.0) {
            integral = 0;
            previousError = 0;
            return 0.0;
        }

        double error = setpoint - currentRPM;

        // Integral with anti-windup
        if (Math.abs(error) > tolerance) {
            integral += error;
        } else {
            integral = 0;
        }

        // Clamp integral
        double iMax = outputMax / Math.max(kI, 1e-6);
        integral = Math.max(-iMax, Math.min(iMax, integral));

        // Optional derivative (usually 0)
        double derivative = error - previousError;

        // Velocity feedforward
        double feedforward = kF * setpoint;

        double output =
                feedforward +
                        kP * error +
                        kI * integral +
                        kD * derivative;

        // Clamp output
        output = Math.max(outputMin, Math.min(outputMax, output));

        previousError = error;
        return output;
    }

}

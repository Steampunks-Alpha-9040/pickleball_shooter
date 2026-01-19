package org.firstinspires.ftc.teamcode.util;

public class PIDposition {

    private double kP, kI, kD, kF;
    private double setpoint;

    private double integral;
    private double previousError;

    private double outputMin = -1.0;
    private double outputMax = 1.0;

    private double tolerance;

    private double clamp;

    private double clampIntegral = 0.1;

    public void setPID(double kP, double kI, double kD, double kF, double tolerance){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(tolerance);
    }

    public PIDposition(double kP, double kI, double kD, double kF, double toleranceRadians) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(toleranceRadians);
        this.clamp = 1;
    }

    public PIDposition(double kP, double kI, double kD, double kF, double toleranceRadians, double clamp) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
        this.tolerance = Math.abs(toleranceRadians);
        this.clamp = clamp;
    }

    public void setSetpoint(double angleRadians) {
        this.setpoint = angleRadians;
    }

    public double getSetpoint(){
        return this.setpoint;
    }

    public double calculate(double currentAngle) {
        double error = setpoint - currentAngle;

        if (Math.abs(error) <= tolerance) {
            integral = 0;
        }

        // Derivative
        double derivative = error - previousError;

        // Feedforward
        double feedforward = (error < 0) ? -kF : kF;

        double output =
                kP * error +
                        kI * integral +
                        kD * derivative +
                        feedforward;

        // Clamp output
        output = Math.max(outputMin, Math.min(outputMax, output));

        // Anti-windup
        if (Math.abs(output) < outputMax /10) {
            integral += error;
        }

        previousError = error;

        if (output > clamp) {
            output = clamp;
        }

        if (output < -clamp) {
            output = -clamp;
        }
        if (Math.abs(error) <= tolerance) {
            output = 0;
        }

        return output;
    }




    public void reset() {
        integral = 0;
        previousError = 0;
    }
}

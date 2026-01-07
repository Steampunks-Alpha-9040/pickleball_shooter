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
            integral = 0.0;
            previousError = 0.0;
            return 0.0;
        }

        double error = setpoint - currentRPM;

        // Derivative per loop
        double derivative = error - previousError;

        // Only integrate when not within tolerance (prevents noise windup)
        if (Math.abs(error) > tolerance) {
            integral += error;

            // clamp integral to prevent windup (tune these)
            double iMin = -5000, iMax = 5000;
            if (integral > iMax) integral = iMax;
            if (integral < iMin) integral = iMin;
        }

        // Feedforward: keep at least ff even within tolerance
        double ff = kF; // or kF * setpoint if that’s how you tuned it

        double output = (kP * error) + (kI * integral) + (kD * derivative) + ff;

        // Clamp output
        if (output > outputMax) output = outputMax;
        if (output < outputMin) output = outputMin;

        previousError = error;
        return output;
    }

}

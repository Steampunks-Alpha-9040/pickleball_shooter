package org.firstinspires.ftc.teamcode.util;

public class ContinuousInputsPID {

    // PIDF gains
    private double kP, kI, kD, kF;

    // State
    private double setPoint = 0.0;
    private double prevError = 0.0;
    private double integral = 0.0;

    // Timing
    private double lastTimestamp = -1.0;
    private double period = 0.0;

    // Integral clamping
    private double minIntegral = -1.0;
    private double maxIntegral = 1.0;

    // Tolerances
    private double positionTolerance = 0.03;
    private double velocityTolerance = Double.POSITIVE_INFINITY;

    // Continuous input (angles, heading, etc.)
    private boolean continuous = false;
    private double minInput, maxInput;

    // Cached errors
    private double positionError = 0.0;
    private double velocityError = 0.0;

    /* -------------------- CONSTRUCTORS -------------------- */

    public ContinuousInputsPID(double kP, double kI, double kD, double kF) {
        setPIDF(kP, kI, kD, kF);
    }

    /* -------------------- CORE CALCULATION -------------------- */

    public double calculate(double measurement) {
        double now = System.nanoTime() * 1e-9;

        if (lastTimestamp < 0) {
            lastTimestamp = now;
            return 0.0;
        }

        period = now - lastTimestamp;
        lastTimestamp = now;

        if (period <= 1e-6) {
            return 0.0;
        }

        // --- Position error ---
        if (continuous) {
            double range = (maxInput - minInput) / 2.0;
            positionError = inputModulus(setPoint - measurement, -range, range);
        } else {
            positionError = setPoint - measurement;
        }

        // --- Integral ---
        integral += positionError * period;
        integral = clamp(integral, minIntegral, maxIntegral);

        // --- Derivative ---
        velocityError = (positionError - prevError) / period;
        prevError = positionError;

        // --- Output ---
        return kP * positionError
                + kI * integral
                + kD * velocityError
                + kF * setPoint;
    }

    public double calculate(double measurement, double setPoint) {
        setSetPoint(setPoint);
        return calculate(measurement);
    }

    /* -------------------- CONTINUOUS INPUT -------------------- */

    public void enableContinuousInput(double minimumInput, double maximumInput) {
        continuous = true;
        minInput = minimumInput;
        maxInput = maximumInput;
    }

    public void disableContinuousInput() {
        continuous = false;
    }

    private static double inputModulus(double input, double min, double max) {
        double modulus = max - min;
        while (input > max) input -= modulus;
        while (input < min) input += modulus;
        return input;
    }

    /* -------------------- UTIL -------------------- */

    public void reset() {
        prevError = 0.0;
        integral = 0.0;
        lastTimestamp = -1.0;
    }

    public boolean atSetPoint() {
        return Math.abs(positionError) < positionTolerance
                && Math.abs(velocityError) < velocityTolerance;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /* -------------------- SETTERS -------------------- */

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public void setPIDF(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public void setIntegrationBounds(double min, double max) {
        minIntegral = min;
        maxIntegral = max;
    }

    public void setTolerance(double positionTol, double velocityTol) {
        positionTolerance = positionTol;
        velocityTolerance = velocityTol;
    }

    /* -------------------- GETTERS -------------------- */

    public double getPositionError() {
        return positionError;
    }

    public double getVelocityError() {
        return velocityError;
    }

    public double getPeriod() {
        return period;
    }

    public double getSetPoint() {
        return setPoint;
    }

    public void setPID(double kP, double kI, double kD, double kF){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }
}
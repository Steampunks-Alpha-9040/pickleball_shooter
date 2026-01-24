package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.geometry.Pose;

public class TimeValueBuffer {
    private final Pose[] values;
    private final double[] times;
    private int index = 0;
    private boolean filled = false;

    public TimeValueBuffer() {
        int size = 50;
        values = new Pose[size];
        times = new double[size];
    }

    public void add(double timeSeconds, Pose value) {
        values[index] = value;
        times[index] = timeSeconds;

        index = (index + 1) % values.length;
        if (index == 0) filled = true;
    }

    // Get value closest to (currentTime - delta)
    public Pose getValueSecondsAgo(double currentTime, double secondsAgo) {
        double targetTime = currentTime - secondsAgo;
        int count = filled ? values.length : index;

        Pose closestValue = null;
        double smallestDiff = Double.MAX_VALUE;

        for (int i = 0; i < count; i++) {
            double diff = Math.abs(times[i] - targetTime);
            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestValue = values[i];
            }
        }
        return closestValue;
    }
}

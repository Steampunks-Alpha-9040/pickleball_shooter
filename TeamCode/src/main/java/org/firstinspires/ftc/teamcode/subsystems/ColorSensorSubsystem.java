package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import dev.nextftc.core.subsystems.Subsystem;

/**
 * Color sensor subsystem compatible with standard FTC SDK and NextFTC.
 */
public class ColorSensorSubsystem implements Subsystem {

    // Singleton instance for easy access
    public static ColorSensorSubsystem INSTANCE;

    private final ColorSensor sensor;
    private final Telemetry telemetry;

    /**
     * Constructor – call this in your OpMode onInit()
     */
    public ColorSensorSubsystem(HardwareMap hardwareMap, Telemetry telemetry, String sensorName) {
        this.sensor = hardwareMap.get(ColorSensor.class, sensorName);
        this.telemetry = telemetry;
        INSTANCE = this; // set singleton
    }

    /**
     * Detects the dominant color: Red, Green, or Blue
     */
    public String detectColor() {
        int r = sensor.red();
        int g = sensor.green();
        int b = sensor.blue();

        String colorName = "Unknown";
        if (r > g && r > b) colorName = "Red";
        else if (g > r && g > b) colorName = "Green";
        else if (b > r && b > g) colorName = "Blue";

        telemetry.addData("Detected Color", colorName);
        telemetry.addData("RGB", "R=%d G=%d B=%d", r, g, b);
        telemetry.update();

        return colorName;
    }

    // Raw sensor values
    public double alpha() { return sensor.alpha(); }
    public double red() { return sensor.red(); }
    public double green() { return sensor.green(); }
    public double blue() { return sensor.blue(); }
}

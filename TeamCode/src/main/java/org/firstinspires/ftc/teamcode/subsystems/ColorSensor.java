package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ColorSensor {
    private NormalizedColorSensor sensor;

    public ColorSensor(HardwareMap hardwareMap, String name) {
        sensor = hardwareMap.get(NormalizedColorSensor.class, name);
    }

    public String getColor() {
        NormalizedRGBA c = sensor.getNormalizedColors();
        float r = c.red;
        float g = c.green;
        float b = c.blue;

        if (g > r && g > b) {
            return "GREEN";
        } else if (r > 0.4 && b > 0.4 && Math.abs(r - b) < 0.15) {
            return "PURPLE";
        } else {
            return "UNKNOWN";
        }
    }
}

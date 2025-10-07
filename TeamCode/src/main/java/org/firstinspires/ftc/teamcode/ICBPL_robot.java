package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Logger;

public class ICBPL_robot extends Robot {

    private static ICBPL_robot bot = null;

    private final Logger logger;

    public enum OpModeType{
        MainOP,
        AUTO,
    }

    public ICBPL_robot(OpModeType mode, HardwareMap hMap, Telemetry log) {
        logger = new Logger(log);
    }






}

package org.firstinspires.ftc.teamcode.util;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Logger extends SubsystemBase {

    private Telemetry logger;


    public Logger(Telemetry log){
        this.logger = log;
    }

    @Override
    public void periodic() {
        logger.update();
    }

    public void addLogged(String key, Object value){
        logger.addData(key, value);
    }
}

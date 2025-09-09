package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Logger;
import org.firstinspires.ftc.teamcode.subsystems.flywheel.FlywheelProto;

public class ICBPL_robot extends Robot {

    private static ICBPL_robot bot = null;

    private final Logger logger;

    private final FlywheelProto flywheelProto;

    public enum OpModeType{
        TELEOP,
        AUTO
    }

    public ICBPL_robot(OpModeType mode, HardwareMap hMap, Telemetry log){
        if (mode == OpModeType.TELEOP) initTeleOp();
        else if (mode == OpModeType.AUTO) initAuto();

        logger = new Logger(log);
        flywheelProto = new FlywheelProto(hMap, logger);
    }

    private void initAuto(){
        this.schedule();
        this.register(flywheelProto, logger);
    }

    private void initTeleOp(){
        this.schedule();
        this.register(flywheelProto, logger);
    }


    /***Flywheel Commands***/
    public InstantCommand spinFlywheel(){
        return new InstantCommand(flywheelProto::spinWheel, flywheelProto);
    }
    public InstantCommand stopFlywheel(){
        return new InstantCommand(flywheelProto::stopWheel, flywheelProto);
    }


}

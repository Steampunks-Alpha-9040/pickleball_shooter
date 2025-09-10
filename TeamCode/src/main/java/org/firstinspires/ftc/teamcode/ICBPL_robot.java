package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeProto;
import org.firstinspires.ftc.teamcode.util.Logger;
import org.firstinspires.ftc.teamcode.subsystems.flywheel.FlywheelProto;

public class ICBPL_robot extends Robot {

    private static ICBPL_robot bot = null;

//    private final Logger logger;

    private FlywheelProto flywheelProto;
    private IntakeProto intakeProto;

    public enum OpModeType{
        MainOP,
        AUTO,
        INTAKEPROTO
    }

    public ICBPL_robot(OpModeType mode, HardwareMap hMap, Telemetry log){
        if (mode == OpModeType.MainOP) initMainOp(hMap);
        else if (mode == OpModeType.AUTO) initAuto(hMap);
        else if (mode == OpModeType.INTAKEPROTO) initIntakeProto(hMap);


//        logger = new Logger(log);
    }

    private void initAuto(HardwareMap hMap){

        flywheelProto = new FlywheelProto(hMap);
        intakeProto = new IntakeProto(hMap);
        this.schedule();
        this.register(flywheelProto);
    }

    private void initMainOp(HardwareMap hMap){
        flywheelProto = new FlywheelProto(hMap);
        this.schedule();
        this.register(flywheelProto);
    }

    private void initIntakeProto(HardwareMap hMap){
        intakeProto = new IntakeProto(hMap);
        this.schedule();
        this.register(intakeProto);
    }


    /***Flywheel Commands***/
    public InstantCommand spinFlywheel(){
        return new InstantCommand(flywheelProto::spinWheel, flywheelProto);
    }
    public InstantCommand stopFlywheel(){
        return new InstantCommand(flywheelProto::stopWheel, flywheelProto);
    }

    /***Intake Commands***/
    public InstantCommand spinIntake(){
        return new InstantCommand(intakeProto::spin, intakeProto);
    }
    public InstantCommand stopIntake(){
        return new InstantCommand(intakeProto::stop, intakeProto);
    }


}

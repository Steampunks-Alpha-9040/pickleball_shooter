package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeProto;
import org.firstinspires.ftc.teamcode.util.Logger;
import org.firstinspires.ftc.teamcode.subsystems.flywheel.FlywheelProto;

public class ICBPL_robot extends Robot {

    private static ICBPL_robot bot = null;

    private final Logger logger;

    private final FlywheelProto flywheelProto;
    private final IntakeProto intakeProto;

    public enum OpModeType{
        MainOP,
        AUTO,
        INTAKEPROTO
    }

    public ICBPL_robot(OpModeType mode, HardwareMap hMap, Telemetry log, GamepadEx gP1){
        if (mode == OpModeType.MainOP) initMainOp();
        else if (mode == OpModeType.AUTO) initAuto();
        else if (mode == OpModeType.INTAKEPROTO) initIntakeProto();


        logger = new Logger(log);
        flywheelProto = new FlywheelProto(hMap, logger);
        intakeProto = new IntakeProto(hMap, logger);
    }

    private void initAuto(){
        this.schedule();
        this.register(flywheelProto, logger);
    }

    private void initMainOp(){
        this.schedule();
        this.register(flywheelProto, logger);
    }

    private void initIntakeProto(){
        this.schedule();
        this.register(intakeProto, logger);
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

    /***Drive Commands***/

    public Command driveRobot(){

    }

}

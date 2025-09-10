package org.firstinspires.ftc.teamcode;


import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.Robot;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.teamcode.subsystems.drivebase.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.drivebase.commands.DriveRobot;
import org.firstinspires.ftc.teamcode.subsystems.intake.IntakeProto;
import org.firstinspires.ftc.teamcode.subsystems.flywheel.FlywheelProto;

public class ICBPL_robot extends Robot {

    private static ICBPL_robot bot = null;
    

    private FlywheelProto flywheelProto;
    private IntakeProto intakeProto;
    private Drivebase drivebaseProto;

    public enum OpModeType{
        MainOP,
        AUTO,
        INTAKEPROTO
    }

    public ICBPL_robot(OpModeType mode, HardwareMap hMap, TelemetryImpl log, GamepadEx gP1){
        if (mode == OpModeType.MainOP) initMainOp(hMap, log);
        else if (mode == OpModeType.AUTO) initAuto(hMap, log);
        else if (mode == OpModeType.INTAKEPROTO) initIntakeProto(hMap, log);

    }

    private void initAuto(HardwareMap hMap, TelemetryImpl telemetry){
        flywheelProto = new FlywheelProto(hMap, telemetry);
        intakeProto = new IntakeProto(hMap, telemetry);
        this.schedule();
    }

    private void initMainOp(HardwareMap hMap, TelemetryImpl telemetry){
        flywheelProto = new FlywheelProto(hMap, telemetry);
        this.schedule();
    }

    private void initIntakeProto(HardwareMap hMap, TelemetryImpl telemetry){
        intakeProto = new IntakeProto(hMap, telemetry);
        this.schedule();
    }

    private void initDriveProto(HardwareMap hMap, TelemetryImpl telemetry, GamepadEx gamepad){
        drivebaseProto = new Drivebase(hMap, telemetry, gamepad);
        this.schedule(new DriveRobot(drivebaseProto));
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

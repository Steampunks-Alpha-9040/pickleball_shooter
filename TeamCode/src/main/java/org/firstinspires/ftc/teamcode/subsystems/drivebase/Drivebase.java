package org.firstinspires.ftc.teamcode.subsystems.drivebase;


import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Drivebase extends SubsystemBase {

    private final GamepadEx gamepad;

    private final Motor FL_motor;
    private final Motor FR_motor;
    private final Motor BL_motor;
    private final Motor BR_motor;

    public Drivebase(HardwareMap hMap, Telemetry telemetry, GamepadEx gamepad){
        super();
        this.FL_motor = new Motor(hMap, Constants.Drivetrain.FL_name, Motor.GoBILDA.RPM_312);
        this.BL_motor = new Motor(hMap, Constants.Drivetrain.BL_name, Motor.GoBILDA.RPM_312);
        this.BR_motor = new Motor(hMap, Constants.Drivetrain.BR_name, Motor.GoBILDA.RPM_312);
        this.FR_motor = new Motor(hMap, Constants.Drivetrain.FR_name, Motor.GoBILDA.RPM_312);

        this.gamepad = gamepad;

    }

    @Override
    public void periodic(){

    }

    public void drive(){

        double vert = gamepad.getLeftY();
        double strafe = gamepad.getLeftX() * 1.1;
        double turn = gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);

        double fl = (turn)+(vert)+(-strafe);
        double fr = (turn)+(-vert)+(strafe);
        double bl = (turn)+(vert)+(strafe);
        double br = (turn)+(-vert)+(-strafe);

        FL_motor.set(fl);
        BL_motor.set(bl);
        BR_motor.set(br);
        FR_motor.set(fr);
    }





}

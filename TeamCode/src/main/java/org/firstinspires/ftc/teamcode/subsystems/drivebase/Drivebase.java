package org.firstinspires.ftc.teamcode.subsystems.drivebase;


import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
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
        FL_motor.set(

                gamepad.getLeftY()
        );
        BL_motor.set(
                gamepad.getLeftY()
        );
        BR_motor.set(
                gamepad.getLeftY()
        );
        FR_motor.set(
                gamepad.getLeftY()
        );

    }





}

package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class FlywheelProto extends SubsystemBase {

    private Telemetry flywheelLog;

    private Motor motor;

    public FlywheelProto (HardwareMap hardwareMap, Telemetry telemetry) {
        this.motor = new Motor(hardwareMap, Constants.FlywheelVerticalTopOne.motorName, Motor.GoBILDA.RPM_435);
        this.flywheelLog = telemetry;
    }

    @Override
    public void periodic() {
        flywheelLog.addData("Flywheel/DutyCycle", motor.motor.getPower());
        flywheelLog.addData("Flywheel/")
    }

}

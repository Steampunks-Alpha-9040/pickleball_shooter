package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class Turret extends SubsystemBase {

    private Motor flywheel;
    private CRServo aiming_M;
    private CRServo aiming_S;

    private Telemetry telemetry;

    private double setpointVelo;

    public Turret(HardwareMap hmap, Telemetry telemetry){
        this.telemetry = telemetry;
        flywheel = new Motor(hmap, Constants.Flywheel.flywheelName, Motor.GoBILDA.BARE);
        aiming_M = new CRServo(hmap, Constants.Flywheel.turretMasterName);
        aiming_M = new CRServo(hmap, Constants.Flywheel.turretSlaveName);
    }

    @Override
    public void periodic() {
        telemetry.addData("Flywheel Velo", flywheel.getCorrectedVelocity() * flywheel.getCPR());
        telemetry.addData("FLywheel Velo Setpoint", setpointVelo);
    }

    public void spinFlywheel(double rps){
        setpointVelo = rps / flywheel.getMaxRPM();
        flywheel.set(setpointVelo);
    }

    public void stopFlywheel(){
        flywheel.stopMotor();
    }

    public void rotateTurret(double angleShoot){
        aiming_M.setTargetDistance();
    }


}

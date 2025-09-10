package org.firstinspires.ftc.teamcode.subsystems.flywheel;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Logger;

public class FlywheelProto extends SubsystemBase {

    private Logger log;

    private final Motor flywheel;
    private final Motor slaveFlywheel;

    public FlywheelProto(final HardwareMap hardwareMap) {
        super();
        this.flywheel = new Motor(hardwareMap, Constants.FlywheelVerticalTopOne.flywheelName, Motor.GoBILDA.RPM_1150);
        this.slaveFlywheel = new Motor(hardwareMap, Constants.FlywheelVerticalTopOne.slaveFlywheelname, Motor.GoBILDA.RPM_1150);

        this.flywheel.setRunMode(Motor.RunMode.VelocityControl);
        this.slaveFlywheel.setRunMode(Motor.RunMode.VelocityControl);

        this.flywheel.setVeloCoefficients(10,10,10);
        this.flywheel.setFeedforwardCoefficients(10,10,0);
        this.flywheel.motor.setDirection(DcMotorSimple.Direction.FORWARD);

        this.slaveFlywheel.setVeloCoefficients(10,10,10);
        this.slaveFlywheel.setFeedforwardCoefficients(10,10,0);
        this.slaveFlywheel.motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        log.addLogged("Flywheel/Master/DutyCycle", flywheel.motor.getPower());
        log.addLogged("Flywheel/Master/Direction", flywheel.motor.getDirection());

        log.addLogged("Flywheel/Slave/DutyCycle", slaveFlywheel.motor.getPower());
        log.addLogged("Flywheel/Slave/Direction", flywheel.motor.getDirection());
    }

    public void spinWheel(){
        flywheel.motor.setPower(Constants.FlywheelVerticalTopOne.spinSpeed_DUTYCYCLE);
        slaveFlywheel.motor.setPower(Constants.FlywheelVerticalTopOne.spinSpeed_DUTYCYCLE);
    }
    public void stopWheel(){
        flywheel.stopMotor();
        slaveFlywheel.stopMotor();
    }





}

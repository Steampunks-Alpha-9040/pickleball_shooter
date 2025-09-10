package org.firstinspires.ftc.teamcode.subsystems.intake;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Logger;

public class IntakeProto extends SubsystemBase {
    private Logger log;

    private final Motor intake;

    public IntakeProto(final HardwareMap hardwareMap) {
        super();
        this.intake = new Motor(hardwareMap, Constants.Intake.intakeName, Motor.GoBILDA.RPM_312);
//        this.log = telemetry;

        this.intake.setRunMode(Motor.RunMode.VelocityControl);

        this.intake.setVeloCoefficients(10,10,10);
        this.intake.setFeedforwardCoefficients(10,10,0);
        this.intake.motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
//        log.addLogged("Flywheel/Master/DutyCycle", intake.motor.getPower());
//        log.addLogged("Flywheel/Master/Direction", intake.motor.getDirection());
    }

    public void spin(){
        intake.motor.setPower(Constants.Intake.spinSpeed_DUTYCYCLE);
    }
    public void stop(){
        intake.stopMotor();
    }

}

package org.firstinspires.ftc.teamcode.subsystems;


import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.SubsystemBase;
import org.firstinspires.ftc.teamcode.util.Util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.modifier.Modifier;
import kotlin.annotation.MustBeDocumented;


public class Turret extends SubsystemBase {

    private static Turret INSTANCE;

    private final SubsystemObjectCell<CRServo> turretServoM =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretMasterName));

    private final SubsystemObjectCell<CRServo> turretServoS =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretSlaveName));

    private final SubsystemObjectCell<AnalogInput> encoder =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(AnalogInput.class, Constants.TurretConstants.encoderName));

    private final PIDFController turretPIDController = Constants.TurretConstants.turretPID;

    private double previousAbsolutePosition;
    private double currentAbsolutePosition;
    private double currentPosition;
    private double setpointPosition;


    public Turret() {

    }

    public static CRServo getTurretMaster(){
        return INSTANCE.turretServoM.get();
    }

    public static CRServo getTurretSlave(){
        return INSTANCE.turretServoS.get();
    }

    public static AnalogInput getEncoder(){
        return INSTANCE.encoder.get();
    }



    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(doNothing());
    }


    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode){
        updateTurretPosition();
    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}


    @NonNull
    public Command aimTurret(double angle){
        return new Lambda("turretFollowTag")
                .addRequirements(INSTANCE)
                .setInit(() -> setTurretTarget(angle))
                .setExecute(this::moveToTurretTarget)
                .setFinish(this::isTurretInTolerance);
    }

    @NonNull
    public Command doNothing(){
        return new Lambda("doNothingTurret");
    }

    public void setTurretTarget(double turretTarget){
        setpointPosition = turretTarget;
        turretPIDController.setSetPoint(turretTarget);
    }

    public void moveToTurretTarget(){
        double pidTarget = turretPIDController.calculate(getCurrentPosition());
    }

    public void updateTurretPosition(){
        currentAbsolutePosition = getEncoder().getVoltage() / 3.2 * 360; // checks current pos before the check if we changed a rotation
        if (Math.abs(previousAbsolutePosition - currentAbsolutePosition) > 355){
            currentPosition++;
        }
        previousAbsolutePosition = currentAbsolutePosition; //sets the previous after
        currentPosition += currentAbsolutePosition;
    }

    public double getCurrentPosition(){
        return currentPosition * Constants.TurretConstants.servoToTurret;
    }

    public boolean isTurretInTolerance(){
        return Util.isBetween(getCurrentPosition(), setpointPosition, Constants.TurretConstants.turretTolerance);
    }



}

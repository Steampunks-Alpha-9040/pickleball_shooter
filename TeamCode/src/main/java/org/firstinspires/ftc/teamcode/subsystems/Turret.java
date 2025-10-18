package org.firstinspires.ftc.teamcode.subsystems;


import androidx.annotation.NonNull;

import com.bylazar.ftcontrol.panels.plugins.html.primitives.P;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;

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
import kotlin.annotation.MustBeDocumented;


public class Turret implements Subsystem {

    private static Turret INSTANCE;

    private final SubsystemObjectCell<DcMotorEx> turretFlywheelMotor =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.TurretConstants.flywheelName));

    private final SubsystemObjectCell<CRServo> turretServoM =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretMasterName));

    private final SubsystemObjectCell<CRServo> turretServoS =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(CRServo.class, Constants.TurretConstants.turretSlaveName));

    private final SubsystemObjectCell<AnalogInput> encoder =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(AnalogInput.class, Constants.TurretConstants.encoderName));

    private double previousAbsolutePosition;
    private double currentAbsolutePosition;
    private double currentPosition;


    public Turret() {

    }

    public static Turret getTurretInstance(){
        if (INSTANCE == null){
            INSTANCE = new Turret();
        }
        return INSTANCE;
    }

    // the annotation class we use to attach this subsystem
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach{}
    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY
                    .and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    public static DcMotorEx getFlywheel() {
        return INSTANCE.turretFlywheelMotor.get();
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
        setDefaultCommand(runShooter());
    }

    @Override
    public void postUserLoopHook(@NonNull Wrapper opMode) {
        updateTurretPosition();
    }
    @Override
    public void postUserStopHook(@NonNull Wrapper opMode) {

    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public Command runShooter() {
        return new Lambda("runShooter")
                .addRequirements(INSTANCE)
                .setInit(() -> getFlywheel().setPower(0.4))
                .setEnd(interrupted -> {
                    if (!interrupted) getFlywheel().setPower(0.0);
                });
    }

    @NonNull
    public Command stop(){
        return new Lambda()
    }

    @NonNull
    public Command trackTag(){
        return new Lambda("turretFollowTag")
                .addRequirements(INSTANCE)
                .setInit()
                .setExecute()
    }

    /**
    * @param power is in duty cycle (range 0.0-1.0)
     */
    public void setFlywheelPower(double power){
        getFlywheel().setPower(power);
    }

    public void setFlywheelPosition(double position, double speed){
        getTurretMaster().setPower(speed);
    }

    public void updateTurretPosition(){
        currentAbsolutePosition = getEncoder().getVoltage() / 3.2 * 360; // checks current pos before the if
        if (Math.abs(previousAbsolutePosition - currentAbsolutePosition) > 355){
            currentPosition++;
        }
        previousAbsolutePosition = currentAbsolutePosition; //sets the previous after
        currentPosition += currentAbsolutePosition;
    }

    public double getCurrentPosition(){
        return currentPosition * Constants.TurretConstants.servoToTurret;
    }

}

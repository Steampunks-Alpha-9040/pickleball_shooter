package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.SubsystemBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.commands.Command;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import kotlin.annotation.MustBeDocumented;

public class Flywheel extends SubsystemBase {
    private static Flywheel INSTANCE;

    private final SubsystemObjectCell<DcMotorEx> turretFlywheelMotor =
            subsystemCell(() -> FeatureRegistrar.getActiveOpMode().hardwareMap.get(DcMotorEx.class, Constants.TurretConstants.flywheelName));


    public static Flywheel getTurretInstance(){
        if (INSTANCE == null){
            INSTANCE = new Flywheel();
        }
        return INSTANCE;
    }

    public static DcMotorEx getFlywheel() {
        return INSTANCE.turretFlywheelMotor.get();
    }

    public Flywheel(){

    }

    @Override
    public void postUserInitHook(@NonNull Wrapper opMode) {
        setDefaultCommand(doNothing());
    }

    // see the feature dev notes on when to use cleanup vs postStop
    @Override
    public void cleanup(@NonNull Wrapper opMode) {}

    @NonNull
    public Command runShooter() {
        return new Lambda("runShooter")
                .addRequirements(INSTANCE)
                .setInit(() -> getFlywheel().setPower(0.4));
    }

    @NonNull
    public Command stopShooter(){
        return new Lambda("Stop Shooter")
                .addRequirements(INSTANCE)
                .setInit(() -> setFlywheelPower(0.0));
    }

    @NonNull
    public Command doNothing(){
        return new Lambda("doNothingShooter")
                .addRequirements(INSTANCE);
    }


    /**
     * @param power is in duty cycle (range 0.0-1.0)
     */
    public void setFlywheelPower(double power){
        getFlywheel().setPower(power);
    }


}

package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.util.Util;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToPosition;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.MotorEx;


public class Flywheel implements Subsystem {

    public static final Flywheel INSTANCE = new Flywheel();

    private MotorEx flywheel;

    private CRServoEx hood;

    private AnalogInput encoder;

    private double encoderRotations;

    private double curEncoder = 0.0;
    private double prevEncoder = 0.0;
    private double turretShotDistance;

    private final ControlSystem flywheelCalculator = ControlSystem.builder()
            .velPid(
                    Constants.FlywheelConstants.flywheel_kP,
                    Constants.FlywheelConstants.flywheel_kI,
                    Constants.FlywheelConstants.flywheel_kD
            )
            .basicFF(
                    Constants.FlywheelConstants.flywheel_kF
            )
            .build();

    private final ControlSystem hoodCalculator = ControlSystem.builder()
            .posPid(
                    Constants.FlywheelConstants.hood_kP,
                    Constants.FlywheelConstants.hood_kI,
                    Constants.FlywheelConstants.hood_kD
            )
            .basicFF(
                    Constants.FlywheelConstants.hood_kF
            )
            .build();


    private Flywheel(){}

    @Override
    public void initialize(){
        flywheel = new MotorEx(Constants.FlywheelConstants.flywheelName).brakeMode().zeroed().reversed();
        hood = new CRServoEx(Constants.FlywheelConstants.hoodName);
        encoder = ActiveOpMode.hardwareMap().get(AnalogInput.class, "hoodEnc");
    }

    @Override
    public void periodic(){
        flywheel.setPower(flywheelCalculator.calculate(flywheel.getState()));
        turretShotDistance = calculateShotDistance();
//        hood.setPower(hoodCalculator.calculate());
    }

    public void log(PanelsTelemetry telemetry){
        telemetry.getTelemetry().addData("flyVeloRPS:", flywheel.getVelocity() * Util.GoBILDA.BARE.getCPR());
    }

    public double calculateShotDistance(){
        switch (Constants.OpModeConstants.side){
            case RED:
                double a = Math.pow(
                        Constants.OpModeConstants.REDscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH), 2
                );
                double b = Math.pow(
                        Constants.OpModeConstants.REDscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH), 2
                );
                return Math.sqrt(a+b);
            case BLUE:
                double c = Math.pow(
                        Constants.OpModeConstants.BLUEscore.getX() - Drivebase.INSTANCE.getBotpose().getX(DistanceUnit.INCH), 2
                );
                double d = Math.pow(
                        Constants.OpModeConstants.BLUEscore.getY() - Drivebase.INSTANCE.getBotpose().getY(DistanceUnit.INCH), 2
                );
                return Math.sqrt(c+d);
            default:
                return 0;
        }
    }



    //Commands
    public Command shootFlywheelFar(){
        return new ParallelGroup(
                new RunToVelocity(flywheelCalculator, 1.0).addRequirements(this)
        ).setInterruptible(true);
    }

    public Command shootFlywheelClose(){
        return new ParallelGroup(
            new RunToVelocity(flywheelCalculator, 0.5).addRequirements(this)
        ).named("closeFlywheel");
    }

    public Command stopFlywheel(){
        return new RunToVelocity(flywheelCalculator, 0.0).addRequirements(this);
    }

    public Command spinHoodUp(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(1))
                .requires(this);
    }

    public Command spinHoodDown(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(-1))
                .requires(this);
    }

    public Command stopHood(){
        return new LambdaCommand()
                .setStart(() -> hood.setPower(0.0))
                .requires(this);
    }







}

package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;

import java.util.Optional;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class Vision implements Subsystem {

    public static Vision INSTANCE = new Vision();
    private Limelight3A limelight;

    private double ty;


    public void initialize(){
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

        limelight.start();
    }

    public void periodic(){
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                Pose3D botpose = result.getBotpose();
                ActiveOpMode.telemetry().addData("tx", result.getTx());
                ActiveOpMode.telemetry().addData("ty", result.getTy());
                ty = result.getTy();
                ActiveOpMode.telemetry().addData("Botpose", botpose.toString());
            }
        }
    }

    public double getHorizontalTy(){
        return ty;
    }


}

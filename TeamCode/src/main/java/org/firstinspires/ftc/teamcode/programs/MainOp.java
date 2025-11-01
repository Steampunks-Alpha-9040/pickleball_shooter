package org.firstinspires.ftc.teamcode.programs;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.Main;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.ServoEx;


@TeleOp(name = "Main_PickleTeleOp")
public class MainOp extends LinearOpMode {




    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.get(Servo.class, "servo");
        servo.setPosition(0.0);
        servo.setDirection(Servo.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive()){
            servo.setPosition(0.0);
        }

    }
}

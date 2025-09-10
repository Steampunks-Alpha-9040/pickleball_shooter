package org.firstinspires.ftc.teamcode.subsystems.drivebase.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.drivebase.Drivebase;

public class DriveRobot extends RunCommand {


    public DriveRobot(Drivebase drive, GamepadEx gamepad){
        super(drive::drive, drive);
    }



}

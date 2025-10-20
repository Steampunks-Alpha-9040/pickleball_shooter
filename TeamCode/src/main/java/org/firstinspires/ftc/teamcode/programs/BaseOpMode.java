package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.util.BulkReads;

import dev.frozenmilk.mercurial.Mercurial;



@Mercurial.Attach
@Turret.Attach
@Flywheel.Attach
@Feeder.Attach
@BulkReads.Attach
public abstract class BaseOpMode extends OpMode {
}

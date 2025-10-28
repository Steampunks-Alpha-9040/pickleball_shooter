package org.firstinspires.ftc.teamcode.programs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.util.BulkReads;

import dev.frozenmilk.mercurial.Mercurial;



@Mercurial.Attach
@BulkReads.Attach
@Flywheel.Attach
public abstract class BaseOpMode extends OpMode {
}

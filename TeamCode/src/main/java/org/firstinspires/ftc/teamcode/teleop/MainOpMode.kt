package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.seattlesolvers.solverslib.command.CommandOpMode
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import org.firstinspires.ftc.teamcode.commands.DriveDefaultCommand
import org.firstinspires.ftc.teamcode.commands.LaunchSequenceCommand
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem

@TeleOp(name = "Main OpMode")
class MainOpMode : CommandOpMode() {

    private lateinit var driveSubsystem: DriveSubsystem
    private lateinit var launcherSubsystem: LauncherSubsystem
    private lateinit var intakeSubsystem: IntakeSubsystem

    private lateinit var gamepad1Ex: GamepadEx
    private lateinit var gamepad2Ex: GamepadEx

    override fun initialize() {
        for (hub in hardwareMap.getAll(LynxModule::class.java)) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }

        gamepad1Ex = GamepadEx(gamepad1)
        gamepad2Ex = GamepadEx(gamepad2)

        driveSubsystem = DriveSubsystem(hardwareMap, gamepad1Ex, telemetry)
        intakeSubsystem = IntakeSubsystem(hardwareMap, gamepad1Ex, telemetry)
        launcherSubsystem = LauncherSubsystem(hardwareMap, gamepad2Ex, telemetry)

        launcherSubsystem.speed = 0.0
        intakeSubsystem.speed = 100.0

        register(driveSubsystem, launcherSubsystem, intakeSubsystem)

        driveSubsystem.defaultCommand = DriveDefaultCommand(driveSubsystem, gamepad1Ex)

        gamepad2Ex.getGamepadButton(GamepadKeys.Button.Y)
            .whenPressed(LaunchSequenceCommand(launcherSubsystem, intakeSubsystem))
    }

    override fun run() {
        super.run()
        telemetry.update()
    }
}
package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import com.seattlesolvers.solverslib.command.SubsystemBase
import com.seattlesolvers.solverslib.command.button.GamepadButton
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import com.seattlesolvers.solverslib.hardware.servos.ServoEx
import org.firstinspires.ftc.robotcore.external.Telemetry

class IntakeSubsystem(
    val hardwareMap: HardwareMap,
    val gamepad: GamepadEx,
    val telemetry: Telemetry
) : SubsystemBase() {
    private val intakeMotor: MotorEx = MotorEx(hardwareMap, "intakeMotor")
    var launchStopper: ServoEx
    var stopperButton: GamepadButton
    var stopperActive: Boolean = false
    private val resting = 0.0
    private val chosenAngle = resting + 35.0
    var speed: Double = 100.0
    private var previousA = false
    private var previousB = false
    init {
        intakeMotor.setRunMode(Motor.RunMode.VelocityControl)
        intakeMotor.inverted = true
        launchStopper = ServoEx(hardwareMap, "launchStopper", 360.0)
        launchStopper.inverted = true
        launchStopper.set(resting)
        stopperButton = GamepadButton(gamepad, GamepadKeys.Button.Y)
        stopperButton.whenPressed(Runnable {
            stopperActive = !stopperActive
            if (stopperActive) {
                launchStopper.set(chosenAngle)
            } else {
                launchStopper.set(resting)
            }
        })
    }
    override fun periodic() {
        val currentA = gamepad.getButton(GamepadKeys.Button.A)
        val currentB = gamepad.getButton(GamepadKeys.Button.B)
        if (currentA && !previousA && speed <= 250) speed += 50
        if (currentB && !previousB && speed >= 50) speed -= 50
        previousA = currentA
        previousB = currentB
        val targetTicksPerSecond = speed * 537.7 / 60.0
        intakeMotor.setVelocity(targetTicksPerSecond)
        telemetry.addData("Launch Stopper Active", stopperActive)
        telemetry.addData("Launch Stopper Pos", launchStopper.get())
    }
}
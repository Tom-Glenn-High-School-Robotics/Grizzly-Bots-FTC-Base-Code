package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import com.seattlesolvers.solverslib.command.SubsystemBase
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs

class LauncherSubsystem(
    val hardwareMap: HardwareMap,
    val gamepad: GamepadEx,
    private val telemetry: Telemetry
) : SubsystemBase() {

    private val rawMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "launcherMotor")
    private val voltageSensor: VoltageSensor = hardwareMap.voltageSensor.iterator().next()
    private val timer = ElapsedTime()

    var speed: Double = 0.0
    var driverOffset: Double = 0.0

    private var filteredTps = 0.0
    private var errorSum = 0.0
    private var batteryVoltage = 12.0
    private var voltageTimer = 0.5

    private var prevA = false
    private var prevB = false
    private var prevX = false
    private var prevUp = false
    private var prevDown = false

    init {
        rawMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rawMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun getCurrentRpm(): Double = filteredTps * 60.0 / 28.0

    override fun periodic() {
        var dt = timer.seconds()
        timer.reset()
        if (dt > 0.5) dt = 0.0

        voltageTimer += dt
        if (voltageTimer >= 0.5) {
            val v = voltageSensor.voltage
            if (v > 0.0) batteryVoltage = v
            voltageTimer = 0.0
        }

        val a = gamepad.getButton(GamepadKeys.Button.A)
        val b = gamepad.getButton(GamepadKeys.Button.B)
        val x = gamepad.getButton(GamepadKeys.Button.X)
        val up = gamepad.getButton(GamepadKeys.Button.DPAD_UP)
        val down = gamepad.getButton(GamepadKeys.Button.DPAD_DOWN)

        if (a && !prevA && speed <= 5500) speed += 100
        if (b && !prevB && speed >= 500) speed -= 100
        if (x && !prevX) speed = 2600.0
        if (up && !prevUp) driverOffset += 25.0
        if (down && !prevDown) driverOffset -= 25.0

        prevA = a; prevB = b; prevX = x
        prevUp = up; prevDown = down

        filteredTps = (0.3 * rawMotor.velocity) + (0.7 * filteredTps)

        val effectiveSpeed = if (speed > 0.0) (speed + driverOffset).coerceAtLeast(0.0) else 0.0
        val targetTps = effectiveSpeed * 28.0 / 60.0
        val error = targetTps - filteredTps

        if (abs(error) < 95.0) errorSum += error * dt else errorSum = 0.0

        val rawPower = (targetTps * LauncherConstants.kF) +
                (error * LauncherConstants.kP) +
                (errorSum * LauncherConstants.kI)

        val compensatedPower = rawPower * (12.0 / batteryVoltage)
        rawMotor.power = compensatedPower.coerceIn(0.0, 1.0)

        telemetry.addData("Target RPM", effectiveSpeed)
        telemetry.addData("Actual RPM", getCurrentRpm())
        telemetry.addData("Driver Offset", driverOffset)
        telemetry.addData("Voltage", "%.2fV", batteryVoltage)

        telemetry.addData("Live kF", LauncherConstants.kF)
        telemetry.addData("Live kP", LauncherConstants.kP)
        telemetry.addData("Live kI", LauncherConstants.kI)
        telemetry.addData("Live kD", LauncherConstants.kD)
        telemetry.addData("Power", compensatedPower)
    }
}
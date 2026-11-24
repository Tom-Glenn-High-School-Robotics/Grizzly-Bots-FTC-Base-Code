package org.firstinspires.ftc.teamcode.subsystems

import com.acmerobotics.dashboard.config.Config

@Config
object LauncherConstants {
    @JvmField var kF: Double = 0.0006
    @JvmField var kP: Double = 0.0007
    @JvmField var kI: Double = 0.002

    @JvmField var kD: Double = 0.00000
}
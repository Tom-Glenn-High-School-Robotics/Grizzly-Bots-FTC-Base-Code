package org.firstinspires.ftc.teamcode.auton

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.pedropathing.follower.Follower
import com.pedropathing.paths.Path
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.skeletonarmy.marrow.prompts.OptionPrompt
import com.skeletonarmy.marrow.prompts.Prompter
import com.skeletonarmy.marrow.prompts.ValuePrompt
import org.firstinspires.ftc.teamcode.pedroPathing.Constants

@Autonomous(name = "pp test", group = "Test")
class Test : OpMode() {
    enum class Alliance { RED, BLUE }

    private lateinit var prompter: Prompter
    private lateinit var follower: Follower
    private lateinit var paths: List<Path>
    private val dashboard = FtcDashboard.getInstance()

    private var activeAlliance = Alliance.RED
    private var pathState = 0
    private var startDelay = 0.0
    private var startTime = 0.0

    override fun init() {
        telemetry = MultipleTelemetry(telemetry, dashboard.telemetry)

        prompter = Prompter(this)
        prompter.prompt("alliance", OptionPrompt("select alliance", Alliance.RED, Alliance.BLUE))
        prompter.prompt(
            "delay",
            ValuePrompt("start delay(s)", Double::class.javaObjectType, 0.0, 10.0, 0.0, 0.5)
        )

        follower = Constants.createFollower(hardwareMap)
    }

    override fun init_loop() {
        prompter.run()

        telemetry.addData("selected alliance", prompter.getOrDefault("alliance", Alliance.RED).name)
        telemetry.addData("selected delay (s)", prompter.getOrDefault("delay", 0.0))
        telemetry.update()
    }

    override fun start() {
        activeAlliance = prompter.getOrDefault("alliance", Alliance.RED)
        startDelay = prompter.getOrDefault("delay", 0.0)
        startTime = runtime
        pathState = 0

        if (activeAlliance == Alliance.RED) {
            follower.setPose(RedPaths.start)
            paths = listOf(
                RedPaths.path1(), RedPaths.path2(), RedPaths.path3(), RedPaths.path4(),
                RedPaths.path5(), RedPaths.path6(), RedPaths.path7(), RedPaths.path8()
            )
        } else {
            follower.setPose(BluePaths.start)
            paths = listOf(
                BluePaths.path1(), BluePaths.path2(), BluePaths.path3(), BluePaths.path4(),
                BluePaths.path5(), BluePaths.path6(), BluePaths.path7(), BluePaths.path8()
            )
        }

        telemetry.addData("alliance picked", activeAlliance.name)
        telemetry.addData("delay", startDelay)
        telemetry.update()
    }

    override fun loop() {
        follower.update()

        if (!follower.isBusy && pathState in paths.indices) {
            if (pathState > 0 || runtime - startTime >= startDelay) {
                follower.follow(paths[pathState])
                pathState++
            }
        }

        val currentPose = follower.pose()
        val packet = TelemetryPacket()

        packet.put("path state: ", pathState)
        packet.put("alliance: ", activeAlliance.name)
        packet.put("robot x: ", currentPose.x())
        packet.put("robot y: ", currentPose.y())
        packet.put("robot heading: ", Math.toDegrees(currentPose.heading()))

        packet.fieldOverlay()
            .setFill(if (activeAlliance == Alliance.RED) "red" else "blue")
            .fillCircle(currentPose.x(), currentPose.y(), 9.0)
            .setStroke("white")
            .strokeLine(
                currentPose.x(),
                currentPose.y(),
                currentPose.x() + (Math.cos(currentPose.heading()) * 12.0),
                currentPose.y() + (Math.sin(currentPose.heading()) * 12.0)
            )

        dashboard.sendTelemetryPacket(packet)
    }
}
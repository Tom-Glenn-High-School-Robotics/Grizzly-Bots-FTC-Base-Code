package org.firstinspires.ftc.teamcode.auton

import com.pedropathing.api.Paths
import com.pedropathing.api.PoseFactory
import com.pedropathing.paths.Path

object BluePaths {

    private val poseFactory = PoseFactory.degrees().mirrorX(70.75)

    val start = poseFactory.of(56.0, 8.0, 90.0)
    private val path1 = poseFactory.of(10.225, 15.2383, 270.0)
    private val path1Control1 = poseFactory.of(32.8803, 32.2983, 0.0)
    private val point2Start = poseFactory.of(10.225, 15.2383, 270.0)
    private val point2 = poseFactory.of(10.0667, 9.1573, 270.0)
    private val point3Start = poseFactory.of(10.0667, 9.1573, 270.0)
    private val point3 = poseFactory.of(58.9752, 112.5086, 270.0)
    private val point3Control1 = poseFactory.of(22.2282, 120.3562, 0.0)
    private val point4Start = poseFactory.of(58.9752, 112.5086, 270.0)
    private val point4 = poseFactory.of(47.4683, 129.2029, 90.0)
    private val point4Control1 = poseFactory.of(46.6985, 111.1334, 0.0)
    private val point5Start = poseFactory.of(47.4683, 129.2029, 90.0)
    private val point5 = poseFactory.of(59.1391, 111.6759, 270.0)
    private val point5Control1 = poseFactory.of(45.928, 111.7482, 0.0)
    private val point6Start = poseFactory.of(59.1391, 111.6759, 270.0)
    private val point6 = poseFactory.of(21.8356, 46.7545, 180.0)
    private val point6Control1 = poseFactory.of(12.6143, 115.3824, 0.0)
    private val point7Start = poseFactory.of(21.8356, 46.7545, 180.0)
    private val point7 = poseFactory.of(12.8505, 46.8085, 180.0)
    private val point8Start = poseFactory.of(12.8505, 46.8085, 180.0)
    private val point8 = poseFactory.of(58.0788, 22.1342, 90.0)

    fun path1(): Path = Paths.curve(start, path1Control1, path1).linear(start, path1)

    fun path2(): Path = Paths.line(point2Start, point2).linear(point2Start, point2)

    fun path3(): Path = Paths.curve(point3Start, point3Control1, point3).linear(point3Start, point3)

    fun path4(): Path = Paths.curve(point4Start, point4Control1, point4).linear(point4Start, point4)

    fun path5(): Path = Paths.curve(point5Start, point5Control1, point5).linear(point5Start, point5)

    fun path6(): Path = Paths.curve(point6Start, point6Control1, point6).linear(point6Start, point6)

    fun path7(): Path = Paths.line(point7Start, point7).linear(point7Start, point7)

    fun path8(): Path = Paths.line(point8Start, point8).linear(point8Start, point8)

}
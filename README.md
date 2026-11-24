# Grizzly Bots — FTC Competition Base Code (2026–2027 BIOBUZZ)

Competition software architecture and reusable base codebase for **FIRST Tech Challenge Team 11918 (Grizzly Bots — Arcturus)** and the Tom Glenn High School robotics program.

Built on top of the FTC SDK and [SolversLib](https://github.com/FTC-23511/SolversLib-Quickstart), this repository standardizes command-based robot control, closed-loop path following, real-time telemetry, and vision-assisted localization across our competition season.

---

## Tech Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Language & Runtime** | Kotlin / Java (Android SDK) | Core robot control, subsystems, and OpModes |
| **Architecture** | SolversLib | Command-based subsystem isolation and scheduling |
| **Autonomous Pathing** | Pedro Pathing | Bezier curve trajectory generation and closed-loop following |
| **State & Utilities** | Marrow | Streamlined hardware abstractions and state management |
| **Telemetry & Tuning** | FTC Dashboard | Live PID/feedforward tuning, field pose visualization, and graphing |
| **Vision** | Limelight 3A | AprilTag localization and game-piece detection |

---

## System Architecture

The codebase separates hardware I/O, control logic, and autonomous scheduling so multiple programmers can iterate on subsystems in parallel without introducing merge conflicts or breaking autonomous routines.

* **Subsystems (`TeamCode/.../subsystems`):** Hardware-isolated modules (drivetrain, intake/spindexer, hooded flywheel turret, vision) exposing clean command interfaces rather than raw motor power calls.
* **Pathing & Localization (`TeamCode/.../pedroPathing`):** Tuned odometry constants, follower parameters, and velocity/centripetal feedforward profiles for repeatable autonomous trajectories.
* **Autonomous & TeleOp (`TeamCode/.../opmodes`):** Declarative command sequences composed from subsystem primitives, keeping OpModes thin and readable.

---

## Key Engineering Decisions & Trade-offs

* **Command-Based Architecture (SolversLib + Marrow) over Linear OpModes:** Moving away from monolithic loop-based OpModes added initial boilerplate, but eliminated state-bleed bugs between mechanisms and let us run concurrent actions (e.g., spinning up the shooter while tracking a path) cleanly.
* **Pedro Pathing over Standard Odometry Waypoints:** Chose reactive Bezier curve following over strict time-parameterized trajectories. This trades away fixed-time predictability in favor of much stronger recovery from field collisions and wheel slip.
* **Live Telemetry via FTC Dashboard:** Externalized tuning constants (PID gains, flywheel RPM targets, pathing tolerances) so we can iterate on control loops in seconds instead of rebuilding and redeploying the Android APK for every tweak.

---

## Testing & Validation

* **Pathing & Odometry Calibration:** Tuned dead-wheel odometry localization, translational/heading PIDF controllers, and centripetal force scaling via FTC Dashboard live graphs to minimize drift over full-field runs.
* **Hardware-in-the-Loop Verification:** Every subsystem change is validated against live telemetry (target vs. actual velocity, pose error, loop cycle time) before merging into `master`.
* **Unit Testing *(In Progress)*:** Extracting hardware-independent math (angle wrapping, pose transforms, state transitions) into pure Kotlin/Java utilities for automated JUnit testing.

---

## Getting Started

### Prerequisites
* Android Studio (Narwhal 3 Feature Drop or later)
* JDK 17+

### Setup
1. Clone the repository:
   ```bash
   git clone [https://github.com/Tom-Glenn-High-School-Robotics/Grizzly-Bots-FTC-Base-Code.git](https://github.com/Tom-Glenn-High-School-Robotics/Grizzly-Bots-FTC-Base-Code.git)

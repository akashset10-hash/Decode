package org.firstinspires.ftc.teamcode.autos;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Autonomous(name = "blueAuto", group = "Autonomous")
@Configurable
public class blueAuto extends OpMode {

    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private int pathState;
    private Paths paths;

    // Subsystems and Timer
    private Intake intake;
    private Shooter shooter;
    private Timer pathTimer;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        // Initialize Hardware and Subsystems
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        pathTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        // Start position from your visualizer
        follower.setStartingPose(new Pose(56, 8, Math.toRadians(90)));

        paths = new Paths(follower);

        setPathState(0);
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        // Telemetry for debugging
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("Timer", pathTimer.getElapsedTimeSeconds());
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.update(telemetry);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // STEP 1: Start driving Path 1
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // STEP 2: Wait for Path 1, then follow Path 2 to the bucket
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path2);
                    setPathState(2);
                }
                break;

            case 2: // STEP 3: Arrive at Bucket (Ending Position), then START SHOOTER
                if (!follower.isBusy()) {
                    shooter.shoot(); // Turn on Port 0
                    setPathState(3); // Resets timer for the next step
                }
                break;

            case 3: // STEP 4: Wait 1 second for shooter ramp-up, then START INTAKE
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    intake.intake(); // Turn on Port 1 motor and Port 0/1 servos
                    setPathState(4); // Resets timer for the 6-second scoring window
                }
                break;

            case 4: // STEP 5: Run both for 6 seconds to clear the balls
                if (pathTimer.getElapsedTimeSeconds() > 6.0) {
                    intake.stop();
                    shooter.stop();
                    setPathState(-1); // Mission Complete
                }
                break;

            default:
                break;
        }
    }

    public static class Paths {
        public PathChain Path1, Path2;

        public Paths(Follower follower) {
            // Path 1: Move to first waypoint
            Path1 = follower.pathBuilder()
                    .addPath(new BezierLine(new Pose(56, 8, Math.toRadians(90)), new Pose(56, 87, Math.toRadians(135))))
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                    .build();

            // Path 2: Move to the final ending position (30, 113)
            Path2 = follower.pathBuilder()
                    .addPath(new BezierLine(new Pose(56, 87, Math.toRadians(135)), new Pose(30, 113, Math.toRadians(135))))
                    .build();
        }
    }
}
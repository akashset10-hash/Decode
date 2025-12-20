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

@Autonomous(name = "redTriangle", group = "Autonomous")
@Configurable
public class redTriangle extends OpMode {

    private TelemetryManager panelsTelemetry;
    public Follower follower;
    private int pathState;
    private Paths paths;

    private Intake intake;
    private Shooter shooter;
    private Timer pathTimer;

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        pathTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);

        // Final Starting Position
        // X: 122.32, Y: 121.98, Heading: 45 degrees
        follower.setStartingPose(new Pose(122.324, 121.988, Math.toRadians(45)));

        paths = new Paths(follower);

        setPathState(0);
        panelsTelemetry.debug("Status", "Red Auto Initialized");
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

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.update(telemetry);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // STEP 1: Drive to the final scoring position
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // STEP 2: Arrived at position, START SHOOTER
                if (!follower.isBusy()) {
                    shooter.shoot(); // Turn on Port 0
                    setPathState(2);
                }
                break;

            case 2: // STEP 3: Wait 1 second for ramp-up, then START INTAKE
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    intake.intake(); // Turn on Port 1 motor and Servos
                    setPathState(3);
                }
                break;

            case 3: // STEP 4: Run both for 6 seconds to clear the balls
                if (pathTimer.getElapsedTimeSeconds() > 6.0) {
                    intake.stop();
                    shooter.stop();
                    setPathState(-1); // Finished
                }
                break;

            default:
                break;
        }
    }

    public static class Paths {
        public PathChain Path1;

        public Paths(Follower follower) {
            // Final Path Configuration
            // Start: (122.32, 121.98) -> End: (109.21, 108.04)
            // Heading: 45 to 180 degrees
            Path1 = follower.pathBuilder()
                    .addPath(new BezierLine(
                            new Pose(122.324, 121.988, Math.toRadians(45)),
                            new Pose(109.218, 108.042, Math.toRadians(180))))
                    .setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(180))
                    .build();
        }
    }
}
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

@Autonomous(name = "redAuto", group = "Autonomous")
@Configurable
public class redAuto extends OpMode {

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

        // Red Side Starting Position from Visualizer
        // X: 88, Y: 7.22, Heading: 90 degrees
        follower.setStartingPose(new Pose(88, 7.225, Math.toRadians(90)));

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

        // Debugging values to your Panels
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", Math.toDegrees(follower.getPose().getHeading()));
        panelsTelemetry.update(telemetry);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // STEP 1: Start driving to first Red waypoint
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // STEP 2: Wait for Path 1, then move to Red Bucket
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path2);
                    setPathState(2);
                }
                break;

            case 2: // STEP 3: Arrived at Red Ending Position, START SHOOTER
                if (!follower.isBusy()) {
                    shooter.shoot(); // Turn on Hub Port 0
                    setPathState(3);
                }
                break;

            case 3: // STEP 4: Wait 1 second ramp-up, then START INTAKE
                if (pathTimer.getElapsedTimeSeconds() > 1.0) {
                    intake.intake(); // Turn on Hub Port 1 motor and Servos
                    setPathState(4);
                }
                break;

            case 4: // STEP 5: Run both for 6 seconds to clear balls
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
        public PathChain Path1, Path2;

        public Paths(Follower follower) {
            // Path 1: Moving straight up on Red side
            // Rotation: 90 to 45 degrees
            Path1 = follower.pathBuilder()
                    .addPath(new BezierLine(new Pose(88, 8, Math.toRadians(90)), new Pose(88, 87.37, Math.toRadians(45))))
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(45))
                    .build();

            // Path 2: Diagonal to Red Bucket
            // End Point: X: 114.59, Y: 113.41
            Path2 = follower.pathBuilder()
                    .addPath(new BezierLine(new Pose(88, 87.37, Math.toRadians(45)), new Pose(114.59, 113, Math.toRadians(45))))
                    .build();
        }
    }
}
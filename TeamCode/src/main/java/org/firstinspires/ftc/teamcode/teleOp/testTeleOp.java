package org.firstinspires.ftc.teamcode.teleOp;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter; // <<< ADD THIS IMPORT

@TeleOp (group = "TeleOps", name = "Test Tele Op")
public class testTeleOp extends OpMode {

    Intake intake;
    Shooter shooter; // <<< ADD THIS LINE: DECLARE THE SHOOTER
    private Follower follower;
    private TelemetryManager telemetryM;



    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap); // <<< ADD THIS LINE: INITIALIZE THE SHOOTER
        follower = Constants.createFollower(hardwareMap);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        follower.startTeleopDrive();

        telemetry.addData("Shooter Ready", "Uses Square (X) on Gamepad 1");
    }

    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }


    @Override
    public void loop() {
        follower.update();
        telemetryM.update();

        follower.setTeleOpDrive(
                -gamepad1.left_stick_y * 0.8,
                -gamepad1.left_stick_x  * 0.8,
                -gamepad1.right_stick_x  * 0.4,
                true // Robot Centric
        );


        // --- INTAKE CONTROL (Uses A button) ---
        if (gamepad1.a) {
            intake.intake();
            telemetry.addData("Intake", "RUNNING (A)");
        } else {
            intake.stop();
            telemetry.addData("Intake", "STOPPED");
        }

        // --- SHOOTER CONTROL LOGIC (Uses SQUARE/X button) ---
        // When Square (gamepad1.x) is held, the shooter runs. When released, it stops.
        if (gamepad1.right_trigger > 0.1) {
            shooter.shoot();
            telemetry.addData("Shooter", "RUNNING (Square/X)");
        } else {
            shooter.stop();
            telemetry.addData("Shooter", "STOPPED");
        }

        telemetry.update(); // Add update here for better telemetry responsiveness
    }
}
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * Represents the Shooter (Outtake) subsystem for an FTC robot.
 * This class handles controlling a flywheel motor for launching game elements.
 */
public class Shooter {
    // --- CONSTANTS ---
    // The default power level for the shooter/flywheel motor.
    // **TUNE THIS VALUE** based on your mechanism and desired speed/distance.
    // The value should be between 0.0 (off) and 1.0 (full power).
    private static final double DEFAULT_SHOOTER_POWER = 0.85; // Example: 85% power

    // --- SUBSYSTEM OBJECTS ---
    private final DcMotorEx flywheelMotor;

    // --- STATE VARIABLES ---
    private double currentMotorPower = 0.0; // Tracks the currently set power

    /**
     * Constructor for the Shooter subsystem.
     * @param hardwareMap The robot's HardwareMap, used to get the motor object.
     */
    public Shooter(HardwareMap hardwareMap) {
        // Initialize the flywheel motor using the device name "flywheelMotor".
        // **IMPORTANT**: This name MUST match the motor name in your FTC Robot Controller configuration.
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");

        // Set the motor direction. You may need to change this if your shooter spins backwards.
        // If your shooter spins the wrong way, uncomment the line below:
        // flywheelMotor.setDirection(DcMotorEx.Direction.REVERSE);

        // Set zero power behavior: FLOAT allows the motor to spin down naturally; BRAKE stops it faster.
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        // We will start with simple power control.
        // For more advanced speed stability, consider using RunMode.RUN_USING_ENCODER.
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    // --- PUBLIC CONTROL METHODS ---

    /**
     * Starts the shooter motor at the predefined DEFAULT_SHOOTER_POWER.
     */
    public void shoot() {
        setPower(DEFAULT_SHOOTER_POWER);
    }

    /**
     * Stops the shooter motor (sets power to 0.0).
     */
    public void stop() {
        setPower(0.0);
    }

    /**
     * Sets the power of the flywheel motor directly.
     * @param power The desired motor power, clamped between -1.0 and 1.0.
     */
    public void setPower(double power) {
        // Use the Range.clip function to ensure the power is within valid bounds (-1.0 to 1.0).
        currentMotorPower = Range.clip(power, -1.0, 1.0);
        flywheelMotor.setPower(currentMotorPower);
    }

    // --- PUBLIC STATUS METHODS (For Telemetry/Debugging) ---

    /**
     * @return The current power being applied to the motor.
     */
    public double getCurrentPower() {
        return currentMotorPower;
    }

    /**
     * @return True if the shooter is currently running (power is greater than 0.0), false otherwise.
     */
    public boolean isRunning() {
        return currentMotorPower > 0.0;
    }
}
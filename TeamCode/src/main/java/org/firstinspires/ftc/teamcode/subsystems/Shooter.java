package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Shooter {
    private static final double DEFAULT_SHOOTER_POWER = 0.85;

    // --- SUBSYSTEM OBJECTS ---
    private final DcMotorEx flywheelMotor;

    // --- STATE VARIABLES ---
    private double currentMotorPower = 0.0; // Tracks the currently set power
    public Shooter(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    // --- PUBLIC CONTROL METHODS ---
    public void shoot() {
        setPower(DEFAULT_SHOOTER_POWER);
    }
    public void stop() {
        setPower(0.0);
    }
    public void setPower(double power) {
        // Use the Range.clip function to ensure the power is within valid bounds (-1.0 to 1.0).
        currentMotorPower = Range.clip(power, -1.0, 1.0);
        flywheelMotor.setPower(currentMotorPower);
    }

    // --- PUBLIC STATUS METHODS (For Telemetry/Debugging) ---
    public double getCurrentPower() {
        return currentMotorPower;
    }
    public boolean isRunning() {
        return currentMotorPower > 0.0;
    }
}
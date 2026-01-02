package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo; // For Continuous Rotation
import com.qualcomm.robotcore.hardware.Servo;   // For Standard (90 degree)
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Shooter {
    private static final double DEFAULT_SHOOTER_POWER = 0.70;

    // Position 0.5 is typically 90 degrees on a standard servo
    private static final double GATE_OPEN_90 = 0.5;
    private static final double GATE_CLOSED = 0.0;

    private final DcMotorEx flywheelMotor;
    private final Servo intakeServoLeft;

    private double currentMotorPower = 0.0;

    public Shooter(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Standard Servo on Expansion Hub Port 0
        intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft");
        intakeServoLeft.setPosition(GATE_CLOSED);
    }

    public void shoot() {
        setPower(DEFAULT_SHOOTER_POWER);
        intakeServoLeft.setPosition(GATE_OPEN_90); // Moves exactly to 90 degrees
    }

    public void stop() {
        setPower(0.0);
        intakeServoLeft.setPosition(GATE_CLOSED); // Moves back to 0 degrees
    }

    public void setPower(double power) {
        currentMotorPower = Range.clip(power, -1.0, 1.0);
        flywheelMotor.setPower(currentMotorPower);
    }
}
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Shooter {
    // Updated to 70% power as requested
    private static final double DEFAULT_SHOOTER_POWER = 0.70;

    private final DcMotorEx flywheelMotor;
    private double currentMotorPower = 0.0;

    public Shooter(HardwareMap hardwareMap) {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheelMotor");
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void shoot() {
        flywheelMotor.setPower(0.7);
    }

    public void stop() {
        flywheelMotor.setPower(0.0);
    }

    public void setPower(double power) {
        currentMotorPower = Range.clip(power, -1.0, 1.0);
        flywheelMotor.setPower(currentMotorPower);
    }
}
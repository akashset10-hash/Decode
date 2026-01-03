package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private DcMotorEx intakeMotor;
    private CRServo intakeServoRight;
    private Servo intakeServoLeft;

    public Intake (HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intakeServoRight = hardwareMap.get(CRServo.class, "intakeServoRight");
        intakeServoLeft = hardwareMap.get(Servo.class, "intakeServoLeft"); // Line 15

        intakeServoRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intake() {
        intakeMotor.setPower(1.0);
        intakeServoRight.setPower(1.0);
    }

    public void stop() {
        intakeMotor.setPower(0.0);
        intakeServoRight.setPower(0.0);
    }

    public void openGate() {
        intakeServoLeft.setPosition(0.5);
    }

    public void closeGate() {
        intakeServoLeft.setPosition(0);
    }
}
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotorEx intakeMotor;
    private CRServo intakeServoRight;
    private CRServo intakeServoLeft;

    public Intake (HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intakeServoRight = hardwareMap.get(CRServo.class, "intakeServoRight");
        intakeServoLeft = hardwareMap.get(CRServo.class, "intakeServoLeft"); // Line 15

        intakeServoRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intake() {
        intakeMotor.setPower(1.0);
        intakeServoRight.setPower(1.0);
        intakeServoLeft.setPower(1.0);
    }

    public void stop() {
        intakeMotor.setPower(0.0);
        intakeServoRight.setPower(0.0);
        intakeServoLeft.setPower(0.0);
    }
}
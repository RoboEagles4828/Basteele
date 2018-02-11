package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Grabber {

    TalonSRX left, right;

    public Grabber(int leftPort, int rightPort) {
        left = new TalonSRX(leftPort);
        right = new TalonSRX(rightPort);
    }

    public void intake() {
        left.set(ControlMode.PercentOutput, 1);
        right.set(ControlMode.PercentOutput, -1);
    }

    public void outtake() {
        left.set(ControlMode.PercentOutput, -1);
        right.set(ControlMode.PercentOutput, 1);
    }

    public void stop() {
        left.set(ControlMode.PercentOutput, 0);
        right.set(ControlMode.PercentOutput, 0);
    }

    public void set(int speed) {
        left.set(ControlMode.PercentOutput, speed);
        right.set(ControlMode.PercentOutput, speed);
    }
}

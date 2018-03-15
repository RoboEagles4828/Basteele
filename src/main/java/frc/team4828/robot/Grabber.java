package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    private static final double SPEED = 1;
    private Victor leftMotor, rightMotor;
    private DoubleSolenoid switcher;

    public Grabber(int leftMotorPort, int rightMotorPort, int[] switcherPorts) {
        leftMotor = new Victor(leftMotorPort);
        rightMotor = new Victor(rightMotorPort);
        switcher = new DoubleSolenoid(switcherPorts[0], switcherPorts[1]);
    }

    public void set(Value mode) {
        switcher.set(mode);
    }

    public void intake() {
        leftMotor.set(-SPEED);
        rightMotor.set(SPEED);
    }

    public void outtake() {
        leftMotor.set(SPEED);
        rightMotor.set(-SPEED);
    }

    public void stop() {
        leftMotor.set(0);
        rightMotor.set(0);
    }
}

package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    private Victor leftMotor, rightMotor;
    private DoubleSolenoid switcher;

    private static final double SPEED = 1;

    Grabber(int leftMotor, int rightMotor, int[] switcher) {
        this.leftMotor = new Victor(leftMotor);
        this.rightMotor = new Victor(rightMotor);
        this.switcher = new DoubleSolenoid(switcher[0], switcher[1]);
    }

    public void open() {
        switcher.set(DoubleSolenoid.Value.kForward);
    }

    public void close() {
        switcher.set(DoubleSolenoid.Value.kReverse);
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

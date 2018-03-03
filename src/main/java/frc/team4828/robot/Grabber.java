package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    private static final double SPEED = 1;
    private Victor leftMotor, rightMotor;
    private DoubleSolenoid switcher;

    Grabber(int leftMotor, int rightMotor, int[] switcher) {
        this.leftMotor = new Victor(leftMotor);
        this.rightMotor = new Victor(rightMotor);
        this.switcher = new DoubleSolenoid(switcher[0], switcher[1]);
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

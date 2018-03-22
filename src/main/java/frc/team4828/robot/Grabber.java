package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    private static final double SPEED = 1;
    private Victor leftMotor, rightMotor;
    private DoubleSolenoid switcher;
    private Solenoid flipper;

    public Grabber(int leftMotorPort, int rightMotorPort, int[] switcherPorts, int flipperport) {
        leftMotor = new Victor(leftMotorPort);
        rightMotor = new Victor(rightMotorPort);
        switcher = new DoubleSolenoid(switcherPorts[0], switcherPorts[1]);
        flipper = new Solenoid(flipperport);
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

    public void up() {
        flipper.set(true);
    }

    public void down() {
        flipper.set(false);
    }
}

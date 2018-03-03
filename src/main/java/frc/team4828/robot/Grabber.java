package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    Victor leftMotor, rightMotor;
    PneumaticSwitch switcher;

    int cur = 0;

    public static final double SPEED = 1;

    public Grabber(int leftMotor, int rightMotor, int[] switcher, Compressor comp) {
        this.leftMotor = new Victor(leftMotor);
        this.rightMotor = new Victor(rightMotor);
        this.switcher = new PneumaticSwitch(comp, switcher);
    }

    public void open() {
        switcher.set(1);
        cur = 1;
    }

    public void close() {
        switcher.set(-1);
        cur = 0;
    }

    public void toggle() {
        if (cur == 0)
            open();
        else
            close();
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

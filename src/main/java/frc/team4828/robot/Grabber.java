package frc.team4828.robot;

import edu.wpi.first.wpilibj.Victor;

public class Grabber {

    Victor leftMotor, rightMotor;
    PneumaticSwitch switcher;

    int cur = 0;

    public static final double SPEED = 1;

    public Grabber(Victor leftMotor, Victor rightMotor, PneumaticSwitch switcher) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.switcher = switcher;
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

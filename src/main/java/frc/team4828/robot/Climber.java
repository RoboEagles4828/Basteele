package frc.team4828.robot;

import edu.wpi.first.wpilibj.Victor;

public class Climber {

    private Victor leftMotor, rightMotor;

    private static final double DEFAULT_SPEED = 0.5;

    Climber(int leftMotor, int rightMotor) {
        this.leftMotor = new Victor(leftMotor);
        this.rightMotor = new Victor(rightMotor);
    }

    public void up() {
        leftMotor.set(DEFAULT_SPEED);
        rightMotor.set(DEFAULT_SPEED);
    }

    public void down() {
        leftMotor.set(-DEFAULT_SPEED);
        rightMotor.set(-DEFAULT_SPEED);
    }

    public void stop() {
        rightMotor.set(0);
        leftMotor.set(0);
    }

}

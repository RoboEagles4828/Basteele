package frc.team4828.robot;

import edu.wpi.first.wpilibj.Victor;

public class Climber {

    private static final double DEFAULT_SPEED = 0.5;
    private Victor leftMotor, rightMotor;

    /**
     * Climber Code. Pretty self explanatory, up to go up, down to go down, stop to stop.
     * Takes in 2 ports.
     * @param leftMotorPort  Left Motor Port
     * @param rightMotorPort Right Motor Port
     */
    public Climber(int leftMotorPort, int rightMotorPort) {
        leftMotor = new Victor(leftMotorPort);
        rightMotor = new Victor(rightMotorPort);
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

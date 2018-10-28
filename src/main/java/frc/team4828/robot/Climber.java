package frc.team4828.robot;

import edu.wpi.first.wpilibj.Victor;

public class Climber {

    private static final double DEFAULT_SPEED = 1;
    private Victor leftMotor, rightMotor;

    /**
     * Climber for the robot.
     * 
     * @param leftMotorPort Left motor port.
     * @param rightMotorPort Right motor port.
     */
    public Climber(int leftMotorPort, int rightMotorPort) {
        leftMotor = new Victor(leftMotorPort);
        rightMotor = new Victor(rightMotorPort);
    }

    /**
     * Sets the climber going upwards at the default speed
     */
    public void up() {
        leftMotor.set(DEFAULT_SPEED);
        rightMotor.set(DEFAULT_SPEED);
    }

    /**
     * Sets the climber going downwards at the default speed
     */
    public void down() {
        leftMotor.set(-DEFAULT_SPEED);
        rightMotor.set(-DEFAULT_SPEED);
    }

    /**
     * Sets climber speed to 0
     */
    public void stop() {
        rightMotor.set(0);
        leftMotor.set(0);
    }

}

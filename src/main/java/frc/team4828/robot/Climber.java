package frc.team4828.robot;

import edu.wpi.first.wpilibj.Victor;

public class Climber {
    private Victor rightMotor,leftMotor;
    double speed;
    public Climber(int leftMotorPort, int rightMotorPort){
        rightMotor = new Victor(rightMotorPort);
        leftMotor = new Victor(leftMotorPort);

}
    public void climbUp(double speed){
        rightMotor.set(speed);
        leftMotor.set(speed);
    }
    public void climbDown(double speed){
        rightMotor.set(-speed);
        leftMotor.set(-speed);
    }
    public void stopClimb(){
        rightMotor.set(0);
        leftMotor.set(0);
    }

}

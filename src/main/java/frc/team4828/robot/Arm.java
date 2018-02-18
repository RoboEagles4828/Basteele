package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Arm {

    private Victor armMotor;

    public Arm(int port) {
        armMotor = new Victor(port);
    }

    public void setSpd(double speed) {
        armMotor.set(speed);
    }
}

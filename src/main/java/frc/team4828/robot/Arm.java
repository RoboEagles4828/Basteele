package frc.team4828.robot;

import edu.wpi.first.wpilibj.Talon;

public class Arm {

    private Talon armMotor;

    public Arm(Talon armMotor) {
        this.armMotor = armMotor;
    }

    public void setSpeed(double speed) {
        armMotor.set(speed);
    }
}

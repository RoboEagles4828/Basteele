package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class Arm {

    private Talon armMotor;
    private DigitalInput armMin, armMax;

    private static final double DEFAULT_SPEED = 0.5;

    public Arm(Talon armMotor, DigitalInput armMin, DigitalInput armMax) {
        this.armMotor = armMotor;
        this.armMin = armMin;
        this.armMax = armMax;
    }

    public void up() {
        armMotor.set(DEFAULT_SPEED);
    }

    public void up(double speed) {
        armMotor.set(speed);
    }

    public void down() {
        armMotor.set(-DEFAULT_SPEED);
    }

    public void down(double speed) {
        armMotor.set(-speed);
    }

    public void stop() {
        armMotor.set(0);
    }

    public boolean check() {
        return (armMotor.get() > 0 && armMax.get()) || (armMotor.get() < 0 && armMin.get());
    }

}

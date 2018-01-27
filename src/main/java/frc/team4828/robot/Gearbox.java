package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Gearbox {

    private TalonSRX motor1;
    private TalonSRX motor2;
    private PneumaticSwitch switcher;

    private final ControlMode CONTROL_MODE = ControlMode.PercentOutput;
    private final double DEFAULT_SPEED = 0.500;

    public Gearbox(TalonSRX motor1, TalonSRX motor2, PneumaticSwitch switcher) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.switcher = switcher;
    }

    public void drive() {
        motor1.set(CONTROL_MODE, DEFAULT_SPEED);
        motor2.set(CONTROL_MODE, DEFAULT_SPEED);
    }

    public void drive(double speed) {
        motor1.set(CONTROL_MODE, speed);
        motor2.set(CONTROL_MODE, speed);
    }

    public void brake() {
        motor1.set(CONTROL_MODE, 0);
        motor2.set(CONTROL_MODE, 0);
    }

    public void setSwitch(int mode) {
        switcher.set(mode);
    }

}

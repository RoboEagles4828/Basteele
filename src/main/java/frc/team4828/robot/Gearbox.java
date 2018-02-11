package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public class Gearbox {

    private TalonSRX motor1;
    private TalonSRX motor2;
    private PneumaticSwitch switcher;

    private ControlMode controlMode = ControlMode.PercentOutput;
    private final double DEFAULT_SPEED = 0.500;

    public Gearbox(TalonSRX motor1, TalonSRX motor2, PneumaticSwitch switcher) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.switcher = switcher;
    }

    public void drive() {
        motor1.set(controlMode, DEFAULT_SPEED);
        motor2.set(controlMode, DEFAULT_SPEED);
    }

    public void drive(double speed) {
        motor1.set(controlMode, speed);
        motor2.set(controlMode, speed);
    }

    public void brake() {
        motor1.set(controlMode, 0);
        motor2.set(controlMode, 0);
    }

    public double getEnc() {
        return motor1.getSensorCollection().getQuadraturePosition();
    }

    public void setSwitch(int mode) {
        switcher.set(mode);
    }
}

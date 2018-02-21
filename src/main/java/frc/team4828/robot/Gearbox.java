package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Gearbox {

    private TalonSRX motor;
    private PneumaticSwitch switcher;

    private ControlMode controlMode = ControlMode.PercentOutput;
    private final double DEFAULT_SPEED = 0.500;

    public Gearbox(TalonSRX motor, TalonSRX motor1, PneumaticSwitch switcher, boolean reverseEnc) {
        this.motor = motor;
        this.switcher = switcher;
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        motor.setSensorPhase(reverseEnc);
        motor1.set(ControlMode.Follower, motor.getDeviceID());
    }

    public void drive() {
        motor.set(controlMode, DEFAULT_SPEED);
    }

    public void drive(double speed) {
        motor.set(controlMode, speed);
    }

    public void brake() {
        motor.set(controlMode, 0);
    }

    public void zeroEnc() {
        motor.setSelectedSensorPosition(0, 0, 10);
    }

    public double getEnc() {
        return motor.getSelectedSensorPosition(0);
    }

    public void setSwitch(int mode) {
        switcher.set(mode);
    }
}

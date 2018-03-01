package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Gearbox {

    private TalonSRX motor;
    private PneumaticSwitch switcher;

    private final double DEFAULT_SPEED = 0.500;

    private double speed = 0;

    public Gearbox(TalonSRX motor, TalonSRX motor1, PneumaticSwitch switcher, boolean reverseEnc) {
        this.motor = motor;
        this.switcher = switcher;
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        motor.setSensorPhase(reverseEnc);
        motor1.set(ControlMode.Follower, motor.getDeviceID());
    }

    public void update() {
        motor.set(ControlMode.PercentOutput, speed);
    }

    public void drive() {
        speed = DEFAULT_SPEED;
        update();
    }

    public void drive(double speed) {
        this.speed = speed;
        update();
    }

    public void brake() {
        speed = 0;
        update();
    }

    public void change(double change) {
        if (speed >= 0) {
            speed += change;
        } else {
            speed -= change;
        }
        update();
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

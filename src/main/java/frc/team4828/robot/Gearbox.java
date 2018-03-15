package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Gearbox {

    private TalonSRX mainMotor;

    public Gearbox(int motorPort, int followPort, boolean reverseEnc) {
        mainMotor = new TalonSRX(motorPort);
        TalonSRX followMotor = new TalonSRX(followPort);
        mainMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        mainMotor.setSensorPhase(reverseEnc);
        followMotor.set(ControlMode.Follower, mainMotor.getDeviceID());
    }

    public void drive(double speed) {
        mainMotor.set(ControlMode.PercentOutput, speed);
    }

    public void brake() {
        mainMotor.set(ControlMode.PercentOutput, 0);
    }

    public void zeroEnc() {
        mainMotor.setSelectedSensorPosition(0, 0, 10);
    }

    public double getEnc() {
        return mainMotor.getSelectedSensorPosition(0);
    }

    public double get() {
        return mainMotor.getMotorOutputPercent();
    }
}

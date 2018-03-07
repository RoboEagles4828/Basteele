package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Gearbox {

    private TalonSRX mainMotor;

    private double speed = 0;

    public Gearbox(int motorPort, int followPort, boolean reverseEnc) {
        mainMotor = new TalonSRX(motorPort);
        TalonSRX followMotor = new TalonSRX(followPort);
        mainMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        mainMotor.setSensorPhase(reverseEnc);
        followMotor.set(ControlMode.Follower, mainMotor.getDeviceID());
    }

    private void update() {
        mainMotor.set(ControlMode.PercentOutput, speed);
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
        mainMotor.setSelectedSensorPosition(0, 0, 10);
    }

    public double getEnc() {
        return mainMotor.getSelectedSensorPosition(0);
    }

    public double get() {
        return speed;
    }
}

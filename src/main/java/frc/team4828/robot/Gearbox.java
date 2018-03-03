package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Compressor;

public class Gearbox {

    private TalonSRX mainMotor, followMotor;
    private PneumaticSwitch switcher;

    private final double DEFAULT_SPEED = 0.500;

    private double speed = 0;

    public Gearbox(int motorPort, int followPort, int[] switcherPort, boolean reverseEnc, Compressor comp) {
        this.mainMotor = new TalonSRX(motorPort);
        this.followMotor = new TalonSRX(followPort);
        this.switcher = new PneumaticSwitch(comp, switcherPort);
        mainMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        mainMotor.setSensorPhase(reverseEnc);
        followMotor.set(ControlMode.Follower, mainMotor.getDeviceID());
    }

    public void update() {
        mainMotor.set(ControlMode.PercentOutput, speed);
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
        mainMotor.setSelectedSensorPosition(0, 0, 10);
    }

    public double getEnc() {
        return mainMotor.getSelectedSensorPosition(0);
    }

    public void setSwitch(int mode) {
        switcher.set(mode);
    }
}

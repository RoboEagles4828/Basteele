package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dumper {

    private static final double OPEN_VALUE = 0;
    private static final double CLOSE_VALUE = .5;
    private DoubleSolenoid dumper;
    private Servo servo;
    private DigitalInput prox;
    private boolean state = false;

    public Dumper(int[] dumperPorts, int servoPort, int proxPort) {
        dumper = new DoubleSolenoid(dumperPorts[0], dumperPorts[1]);
        servo = new Servo(servoPort);
        prox = new DigitalInput(proxPort);
    }

    public void set(Value mode) {
        dumper.set(mode);
    }

    public void open() {
        servo.set(OPEN_VALUE);
        state = true;
    }

    public void close() {
        servo.set(CLOSE_VALUE);
        state = false;
    }

    public boolean isOpen() {
        return state;
    }

    public boolean hasBlock() {
        return !prox.get();
    }

    public void updateDashboard() {
        SmartDashboard.putBoolean("HasBlock", hasBlock());
        SmartDashboard.putBoolean("IsOpen", isOpen());
    }
}

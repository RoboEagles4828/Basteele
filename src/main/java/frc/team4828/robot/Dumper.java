package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Servo;

public class Dumper {

    private DoubleSolenoid dumper;
    private Servo servo;
    private boolean state = false;

    private static final int OPEN_VALUE = 0;
    private static final int CLOSE_VALUE = 69;

    Dumper(int[] dumperPorts, int servoPort) {
        this.dumper = new DoubleSolenoid(dumperPorts[0], dumperPorts[1]);
        this.servo = new Servo(servoPort);
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
}

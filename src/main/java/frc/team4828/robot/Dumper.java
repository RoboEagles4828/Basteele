package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Servo;

public class Dumper {
    private PneumaticSwitch dumper;
    private Servo servo;
    private boolean curr;

    private static final int OPEN_VALUE = 0;
    private static final int CLOSE_VALUE = 69;

    Dumper(int[] dumperPorts, int servoPort, Compressor comp) {
        this.dumper = new PneumaticSwitch(comp, dumperPorts);
        this.servo = new Servo(servoPort);
    }
    public void set(int mode) {
        dumper.set(mode);
    }
    public void open() {
        servo.set(OPEN_VALUE);
        curr = true;
    }
    public void close() {
        servo.set(CLOSE_VALUE);
        curr = false;
    }
    public boolean isOpen() {
        return curr;
    }
}

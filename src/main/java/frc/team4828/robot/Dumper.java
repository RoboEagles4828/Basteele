package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Servo;

public class Dumper {

    private PneumaticSwitch dumper;
    private Servo servo;
    private boolean state = false;

    private static final int OPEN_VALUE = 0;
    private static final int CLOSE_VALUE = 69;

    public Dumper(Compressor comp, int[] dumperPorts, int servoPort) {
        dumper = new PneumaticSwitch(comp, dumperPorts);
        servo = new Servo(servoPort);
    }

    public void set(int mode) {
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

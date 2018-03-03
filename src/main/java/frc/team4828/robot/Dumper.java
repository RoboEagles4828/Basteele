package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Servo;

public class Dumper {
    PneumaticSwitch dumper;
    Servo servo;
    public Dumper(int[] dumperPorts, int servoPort, Compressor comp) {
        this.dumper = new PneumaticSwitch(comp, dumperPorts);
    }
    public void set(int mode) {
        dumper.set(mode);
    }
    public void open() {

    }
    public void close() {

    }
}

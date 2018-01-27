package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Pneumatic {

    private Compressor comp;
    private DoubleSolenoid sol;

    Pneumatic(int compPort, int solPort1, int solPort2) {
        comp = new Compressor(compPort);
        comp.setClosedLoopControl(true);
        sol = new DoubleSolenoid(solPort1, solPort2);
    }

    public boolean enabled() {
        return comp.enabled();
    }

    public double compValue() {
        return comp.getCompressorCurrent();
    }

    public void forward() {
        sol.set(DoubleSolenoid.Value.kForward);
    }

    public void reverse() {
        sol.set(DoubleSolenoid.Value.kReverse);
    }

    public void off() {
        sol.set(DoubleSolenoid.Value.kOff);
    }

}

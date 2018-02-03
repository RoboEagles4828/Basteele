package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticSwitch {

    private Compressor comp;
    private DoubleSolenoid sol;

    PneumaticSwitch(Compressor comp, DoubleSolenoid sol) {
        this.comp = comp;
        this.sol = sol;
    }

    public boolean enabled() {
        return comp.enabled();
    }

    public double compValue() {
        return comp.getCompressorCurrent();
    }

    public void set(int mode) {
        switch (mode) {
        case 1:
            sol.set(DoubleSolenoid.Value.kForward);
            break;
        case -1:
            sol.set(DoubleSolenoid.Value.kReverse);
            break;
        case 0:
            sol.set(DoubleSolenoid.Value.kOff);
            break;
        default:
            sol.set(DoubleSolenoid.Value.kOff);
            break;
        }
    }

}

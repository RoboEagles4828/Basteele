package frc.team4828.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticSwitch {

    private Compressor comp;
    private DoubleSolenoid sol;

    PneumaticSwitch(Compressor comp, DoubleSolenoid sol) {
        this.comp = comp;
        comp.setClosedLoopControl(true);
        this.sol = sol;
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

    public void set(int mode) {
        switch (mode) {
        case 1:
            forward();
            break;
        case -1:
            reverse();
            break;
        case 0:
            off();
            break;
        default:
            off();
            break;
        }
    }

}

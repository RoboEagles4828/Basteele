package frc.team4828.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Grabber {
    Victor left, right;
    DoubleSolenoid sol;

    int curr = 0;

    public static final double SPEED = 1;

    public Grabber(int leftp, int rightp, int pistlp, int pistrp) {
        left = new Victor(leftp);
        right = new Victor(rightp);
        sol = new DoubleSolenoid(pistlp, pistrp);
    }

    public void grabOpen() {
        sol.set(DoubleSolenoid.Value.kReverse);
        curr = 1;
    }

    public void grabClose() {
        sol.set(DoubleSolenoid.Value.kForward);
        curr = 0;
    }

    public void toggle() {
        if(curr == 0)
            grabOpen();
        else if(curr == 1)
            grabClose();
    }

    public void intake() {
        left.set(-SPEED);
        right.set(SPEED);
    }

    public void outtake() {
        left.set(SPEED);
        right.set(-SPEED);
    }

    public void stop() {
        left.set(0);
        right.set(0);
    }
}

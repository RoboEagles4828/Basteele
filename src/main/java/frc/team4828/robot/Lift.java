package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Lift {

    private LiftThread liftThread;

    public Lift(Victor liftMotor, Victor armMotor, DigitalInput liftMin, DigitalInput liftMax, DigitalInput armMin,
            DigitalInput armMax, DigitalInput switcher) {
        liftThread = new LiftThread(liftMotor, liftMin, liftMax, switcher);
    }

    // Start LiftThread methods

    public void setLiftSpeed(double speed) {
        liftThread.setSpeed(speed);
    }

    public void setLiftPos(int pos) {
        liftThread.setPos(pos);
    }

    public void setLiftTarget(int target) {
        liftThread.setTarget(target);
    }

    public void startLiftThread() {
        liftThread.start();
    }

    public void stopLiftThread() {
        liftThread.stop();
    }

    public void forceStopLiftThread() {
        liftThread.forceStop();
    }

    public void abortLift() {
        liftThread.abort();
    }

    public void resumeLift() {
        liftThread.resume();
    }

    public double getLiftSpeed() {
        return liftThread.getLift();
    }

    public boolean isLiftIdle() {
        return liftThread.isIdle();
    }

    public void setManual(boolean manual) {
        liftThread.setManual(manual);
    }

    public void setLiftSpeedManual(double speed) {
        setLiftSpeed(Math.abs(speed));
        if (speed > 0) {
            setLiftTargetDirection(1);
        } else if (speed < 0) {
            setLiftTargetDirection(-1);
        } else {
            setLiftTargetDirection(0);
        }
    }

    public void setLiftTargetDirection(int targetDirection) {
        liftThread.setTargetDirection(targetDirection);
    }

    // End LiftThread methods
}

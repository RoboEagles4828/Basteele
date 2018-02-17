package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Lift {

    private LiftThread liftThread;
    private ArmThread armThread;

    public Lift(Victor liftMotor, Victor armMotor, DigitalInput liftMin, DigitalInput liftMax, DigitalInput armMin,
            DigitalInput armMax, DigitalInput switcher) {
        liftThread = new LiftThread(liftMotor, liftMin, liftMax, switcher);
        armThread = new ArmThread(armMotor, armMin, armMax);
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

    public void setLift(int direction) {
        liftThread.setLift(direction);
    }

    public void setLiftAbsolute(double speed) {
        liftThread.setLiftAbsolute(speed);
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

    // End LiftThread methods

    // Start ArmThread methods

    public void setArmSpeed(int speed) {
        armThread.setSpeed(speed);
    }

    public void setArmPos(int pos) {
        armThread.setPos(pos);
    }

    public void setArmTarget(int target) {
        armThread.setTarget(target);
    }

    public void startArmThread() {
        armThread.start();
    }

    public void stopArmThread() {
        armThread.stop();
    }

    public void forceStopArmThread() {
        armThread.forceStop();
    }

    public void abortArm() {
        armThread.abort();
    }

    public void resumeArm() {
        armThread.resume();
    }

    public double getArmSpeed() {
        return armThread.getArm();
    }

    public boolean isArmIdle() {
        return armThread.isIdle();
    }
}

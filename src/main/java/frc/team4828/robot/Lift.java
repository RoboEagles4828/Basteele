package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {

    private Talon liftMotor, armMotor, leftGrabber, rightGrabber;
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;

    private LiftThread liftThread;
    private ArmThread armThread;

    public Lift(Talon liftMotor, Talon armMotor, Talon leftGrabber, Talon rightGrabber, DigitalInput liftMin,
            DigitalInput liftMax, DigitalInput armMin, DigitalInput armMax, DigitalInput switcher) {
        this.liftMotor = liftMotor;
        this.armMotor = armMotor;
        this.leftGrabber = leftGrabber;
        this.rightGrabber = rightGrabber;
        this.liftMin = liftMin;
        this.liftMax = liftMax;
        this.armMin = armMin;
        this.armMax = armMax;
        this.switcher = switcher;

        liftThread = new LiftThread(liftMotor, liftMin, liftMax, switcher);
        armThread = new ArmThread(armMotor, armMin, armMax);
    }

    // Start LiftThread methods

    public void setLiftSpeed(int speed) {
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

    // End ArmThread methods

    // Start Grabber methods

    public void intake() {
        leftGrabber.set(1);
        rightGrabber.set(-1);
    }

    public void outtake() {
        leftGrabber.set(-1);
        rightGrabber.set(1);
    }

    public void stopGrabber() {
        leftGrabber.set(0);
        rightGrabber.set(0);
    }

    public void setGrabberSpeed(int speed) {
        leftGrabber.set(speed);
        rightGrabber.set(-speed);
    }

    public double getGrabberSpeed() {
        return leftGrabber.get();
    }

    // End Grabber methods

}

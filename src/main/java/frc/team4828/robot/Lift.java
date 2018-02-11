package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {

    private Talon liftMotor, armMotor, leftGrabber, rightGrabber;
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;
    private Grabber grabber;

    private LiftThread liftThread;

    public Lift(int liftMotorPort, int armMotorPort, int leftGrabberPort, int rightGrabberPort, int liftMinPort,
            int liftMaxPort, int armMinPort, int armMaxPort, int switcherPort) {

        liftMotor = new Talon(liftMotorPort);
        armMotor = new Talon(armMotorPort);
        leftGrabber = new Talon(leftGrabberPort);
        rightGrabber = new Talon(rightGrabberPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
        armMin = new DigitalInput(armMinPort);
        armMax = new DigitalInput(armMaxPort);
        switcher = new DigitalInput(switcherPort);

        liftThread = new LiftThread(liftMotor, liftMin, liftMax, switcher);
    }

    // Start LiftThread methods
    
    public void setSpeed(int speed) {
        liftThread.setSpeed(speed);
    }

    public void setPos(int pos) {
        liftThread.setPos(pos);
    }

    public void setTarget(int target) {
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

    public void abort() {
        liftThread.abort();
    }

    public void resume() {
        liftThread.resume();
    }

    // End LiftThread methods

    public void armUp() {
        armMotor.set(DEFAULT_SPEED);
        while (!armMax.get()) {
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }

    public void armUp(int speed) {
        armMotor.set(speed);
        while (!armMax.get()) {
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }

    public void armDown() {
        armMotor.set(-DEFAULT_SPEED);
        while (!armMin.get()) {
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }

    public void armDown(int speed) {
        armMotor.set(speed);
        while (!armMin.get()) {
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }

    public void intake() {
        leftGrabber.set(1);
        rightGrabber.set(-1);
    }

    public void outtake() {
        leftGrabber.set(-1);
        rightGrabber.set(1);
    }

    public void stop() {
        leftGrabber.set(0);
        rightGrabber.set(0);
    }

    public void set(int speed) {
        leftGrabber.set(speed);
        rightGrabber.set(-speed);
    }
}

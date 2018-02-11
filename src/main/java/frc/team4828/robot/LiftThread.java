package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class LiftThread implements Runnable {

    private Talon liftMotor;
    private DigitalInput liftMin, liftMax, switcher;

    private boolean currentState;
    private boolean prevState;

    private int speed = -1;
    private int pos = 0;
    private int target = 0;
    private boolean stopThread = false;
    private boolean abort = false;
    private int direction = 0;

    public static final double DEFAULT_SPEED = 0.1;
    public static final int ABORT_DELAY = 100;
    public static final int CHECK_DELAY = 10;

    private Thread thread;

    public LiftThread(Talon liftMotor, DigitalInput liftMin, DigitalInput liftMax, DigitalInput switcher) {
        this.liftMotor = liftMotor;
        this.liftMin = liftMin;
        this.liftMax = liftMax;
        this.switcher = switcher;
    }

    public void run() {
        System.out.println("Lift Thread Started");
        while (!stopThread) {
            while (!abort) {
                checkPos();
                try {
                    Thread.sleep(CHECK_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(ABORT_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Lift Thread Stopped");
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPos(int pos) {
        System.out.println("Warning: Position has been manually changed");
        this.pos = pos;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void start() {
        stopThread = false;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        stopThread = true;
    }

    public void forceStop() {
        stopThread = true;
        thread.interrupt();
    }

    public void abort() {
        abort = true;
    }

    public void resume() {
        abort = false;
    }

    private void checkPos() {
        currentState = switcher.get();
        if (currentState != prevState) {
            pos += direction;
        }
        if (pos > target && !liftMin.get()) {
            setLift(-1);
        } else if (pos < target && !liftMax.get()) {
            setLift(1);
        } else {
            setLift(0);
        }
        prevState = currentState;
    }

    private void setLift(int direction) {
        if (speed != -1) {
            liftMotor.set(speed * direction);
        } else {
            liftMotor.set(DEFAULT_SPEED * direction);
        }
        this.direction = direction;
    }
}

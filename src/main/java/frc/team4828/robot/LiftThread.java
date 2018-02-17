package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class LiftThread implements Runnable {

    private Victor liftMotor;
    private DigitalInput liftMin, liftMax, switcher;

    private boolean currentState;
    private boolean prevState;

    private double speed = -1;
    private int pos = 0;
    private int target = 0;
    private boolean stopThread = false;
    private boolean abort = false;
    private int direction = 0;

    private static final double DEFAULT_SPEED = 0.3;
    private static final int ABORT_DELAY = 100;
    private static final int CHECK_DELAY = 10;

    private Thread thread;
    private boolean manual;

    public LiftThread(Victor liftMotor, DigitalInput liftMin, DigitalInput liftMax, DigitalInput switcher) {
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

    public void setSpeed(double speed) {
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
        if ((liftMin.get() && direction == -1) || (liftMax.get() && direction == 1)) {
            setLift(0);
            pos += direction;
        }
        if (!manual) {
            if (pos > target && direction != -1 && !liftMin.get()) {
                setLift(-1);
            } else if (pos < target && direction != 1 && !liftMax.get()) {
                setLift(1);
            }
        }
        prevState = currentState;
    }

    public void setLift(int direction) {
        if (speed != -1) {
            liftMotor.set(speed * direction);
        } else {
            liftMotor.set(DEFAULT_SPEED * direction);
        }
        this.direction = direction;
    }

    public void setLiftAbsolute(double speed) {
        liftMotor.set(speed);
    }

    public double getLift() {
        return liftMotor.get();
    }

    public boolean isIdle() {
        return pos == target;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

}

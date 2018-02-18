package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
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
    private int targetDirection = 0;
    private double startTime = 0;

    private static final double DEFAULT_SPEED = 0.3;
    private static final double ABORT_DELAY = 0.1;
    private static final double CHECK_DELAY = 0.01;
    private static final double SWITCHER_DELAY = 0.05;

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
        speed = -1;
        pos = 0;
        target = 0;
        direction = 0;
        targetDirection = 0;
        while (!stopThread) {
            while (!abort && !stopThread) {
                checkPos();
                Timer.delay(CHECK_DELAY);
            }
            Timer.delay(ABORT_DELAY);
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
        if (currentState != prevState && Timer.getFPGATimestamp() > startTime + SWITCHER_DELAY) {
            pos += direction;
            startTime = Timer.getFPGATimestamp();
        }
        if ((liftMin.get() && direction == -1) || (liftMax.get() && direction == 1)) {
            setLift(0);
            pos += direction;
        }
        if (manual) {
            if (targetDirection == -1 && !liftMin.get()) {
                setLift(-1);
            } else if (targetDirection == 1 && !liftMax.get()) {
                setLift(1);
            } else if (targetDirection == 0) {
                setLift(0);
            }
        } else {
            if (pos > target && !liftMin.get()) {
                setLift(-1);
            } else if (pos < target && !liftMax.get()) {
                setLift(1);
            } else if (pos == target) {
                setLift(0);
            }
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

    public double getLift() {
        return liftMotor.get();
    }

    public boolean isIdle() {
        return pos == target;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public void setTargetDirection(int targetDirection) {
        this.targetDirection = targetDirection;
    }
    
    public void setLiftSpeedManual(double speed) {
        setSpeed(Math.abs(speed));
        if (speed > 0) {
            setTargetDirection(1);
        } else if (speed < 0) {
            setTargetDirection(-1);
        } else {
            setTargetDirection(0);
        }
    }

}

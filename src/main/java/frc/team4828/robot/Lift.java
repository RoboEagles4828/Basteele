package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Lift implements Runnable {

    private static final double DEFAULT_SPEED = 0.3;
    private static final double CHECK_DELAY = 0.01;
    private static final double SWITCHER_DELAY = 0.05;
    private Victor liftMotor;
    private DigitalInput liftMin, liftMax, switcher;
    private boolean prevState = false;
    private double startTime = 0.0;
    private double speed;
    private int direction;
    private int pos;
    private int target;
    private boolean manual;
    private int targetDirection;
    private boolean stopThread = false;

    Lift(int liftMotor, int liftMin, int liftMax, int switcher) {
        this.liftMotor = new Victor(liftMotor);
        this.liftMin = new DigitalInput(liftMin);
        this.liftMax = new DigitalInput(liftMax);
        this.switcher = new DigitalInput(switcher);
    }

    public void run() {
        System.out.println("Lift Thread Started");
        // Init Variables
        speed = -1;
        direction = 0;

        pos = 0;
        target = 0;

        manual = true;
        targetDirection = 0;
        // Main Loop
        while (!stopThread) {
            check();
            set();
            Timer.delay(CHECK_DELAY);
        }
        System.out.println("Lift Thread Stopped");
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Position Control

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isNotIdle() {
        return pos != target;
    }

    // Manual Control

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    private void setTargetDirection(int targetDirection) {
        this.targetDirection = targetDirection;
    }

    public void setSpeedManual(double speed) {
        setSpeed(Math.abs(speed));
        if (speed > 0) {
            setTargetDirection(1);
        } else if (speed < 0) {
            setTargetDirection(-1);
        } else {
            setTargetDirection(0);
        }
    }

    // Thread Control

    public void start() {
        stopThread = false;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        stopThread = true;
    }

    // Check Loop

    private void check() {
        boolean currentState = switcher.get();
        // Position Check
        if (currentState != prevState && Timer.getFPGATimestamp() > startTime + SWITCHER_DELAY) {
            pos += direction;
            startTime = Timer.getFPGATimestamp();
        }
        // Limits Check
        if ((liftMin.get() && direction == -1) || (liftMax.get() && direction == 1)) {
            pos += direction;
            direction = 0;
        }
        // Manual Check
        if (manual) {
            if (targetDirection == -1 && !liftMin.get()) {
                direction = -1;
            } else if (targetDirection == 1 && !liftMax.get()) {
                direction = 1;
            } else if (targetDirection == 0) {
                direction = 0;
            }
        }
        // Auto Check
        else {
            if (pos > target && !liftMin.get()) {
                direction = -1;
            } else if (pos < target && !liftMax.get()) {
                direction = 1;
            } else if (pos == target) {
                direction = 0;
            }
        }
        prevState = currentState;
    }

    // Set Loop

    private void set() {
        if (speed == -1) {
            liftMotor.set(DEFAULT_SPEED * direction);
        } else {
            liftMotor.set(speed * direction);
        }
    }
}

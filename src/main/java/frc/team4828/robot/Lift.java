package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift implements Runnable {

    private static final double DEFAULT_SPEED = 0.5;
    private static final double CHECK_DELAY = 0.01;
    private static final double SWITCHER_DELAY = 0.05;
    private Victor liftMotor;
    private DigitalInput liftMin, liftMax, switcher;
    private int posMax;
    private boolean prevState = false;
    private double startTime = 0.0;
    private double speed;
    private int direction;
    private int pos;
    private int target;
    private boolean manual;
    private int targetDirection;
    private boolean stopThread = false;

    public Lift(int liftMotorPort, int liftMinPort, int liftMaxPort, int switcherPort, int amount) {
        liftMotor = new Victor(liftMotorPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
        switcher = new DigitalInput(switcherPort);
        posMax = amount * 2 + 2;
    }

    public void run() {
        System.out.println("Lift Thread Started");
        // Init Variables
        speed = -1;
        direction = 0;
        pos = 1;
        target = 1;
        manual = false;
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
        this.target = target * 2;
    }

    public void resetTarget() {
        target = pos;
    }

    public int getPos() {
        return pos;
    }

    public boolean isBusy() {
        return pos != target;
    }

    // Manual Control

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public void setTargetDirection(int targetDirection) {
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
        if (currentState != prevState && Timer.getFPGATimestamp() - startTime > SWITCHER_DELAY) {
            pos += direction;
            startTime = Timer.getFPGATimestamp();
        }
        // Limits Check
        if (liftMin.get() && direction == -1) {
            direction = 0;
            pos = 0;
            return;
        } else if (liftMin.get()) {
            pos = direction;
        }
        if (liftMax.get() && direction == 1) {
            direction = 0;
            pos = posMax;
            return;
        } else if (liftMax.get()) {
            pos = posMax + direction;
        }
        // Manual Check
        if (manual) {
            direction = targetDirection;
        }
        // Auto Check
        else {
            if (pos > target) {
                direction = -1;
            } else if (pos < target) {
                direction = 1;
            } else {
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

    public void updateDashboard() {
        SmartDashboard.putBoolean("Lift Min", liftMin.get());
        SmartDashboard.putBoolean("Lift Max", liftMax.get());
        SmartDashboard.putNumber("Lift Speed", speed);
        SmartDashboard.putNumber("Lift Direction", direction);
        SmartDashboard.putNumber("Lift Position", pos);
        SmartDashboard.putNumber("Lift Target", target);
        SmartDashboard.putBoolean("Lift Manual", manual);
        SmartDashboard.putNumber("Lift Target Direction", targetDirection);
    }

}

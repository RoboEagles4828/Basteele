package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift implements Runnable {

    public static final double DEFAULT_SPEED = 0.6;
    private static final double CHECK_DELAY = 0.01;
    private Victor liftMotor;
    private DigitalInput liftMin, liftMax;
    private double speed;
    private int direction;
    private int targetDirection;
    private boolean stopThread = true;

    public Lift(int liftMotorPort, int liftMinPort, int liftMaxPort, int switcherPort) {
        liftMotor = new Victor(liftMotorPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
    }

    public void run() {
        System.out.println("Lift Thread Started");
        // Init Variables
        speed = DEFAULT_SPEED;
        direction = 0;
        targetDirection = 0;
        // Main Loop
        while (!stopThread) {
            check();
            set();
            Timer.delay(CHECK_DELAY);
        }
        System.out.println("Lift Thread Stopped");
    }

    public boolean isLimited() {
        return (liftMin.get() && targetDirection == -1) || (liftMax.get() && targetDirection == 1);
    }

    public synchronized void setSpeed(double speed) {
        this.speed = speed;
    }

    public synchronized void setDirection(int direction) {
        targetDirection = direction;
    }

    public synchronized void setVelocity(double velocity) {
        speed = Math.abs(velocity);
        targetDirection = (int) Math.signum(velocity);
    }

    public synchronized boolean isBusy() {
        return targetDirection != 0 && !isLimited();
    }

    public synchronized boolean isMoving() {
        return direction != 0;
    }

    // Thread Control

    public synchronized void start() {
        if (stopThread) {
            stopThread = false;
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stop() {
        stopThread = true;
    }

    // Check Loop

    private synchronized void check() {
        if (isLimited()) {
            direction = 0;
            return;
        }
        direction = targetDirection;
    }

    private synchronized void set() {
        liftMotor.set(speed * direction);
    }

    public synchronized void updateDashboard() {
        SmartDashboard.putBoolean("Lift Min", liftMin.get());
        SmartDashboard.putBoolean("Lift Max", liftMax.get());
        SmartDashboard.putNumber("Lift Speed", speed);
        SmartDashboard.putNumber("Lift Direction", direction);
        SmartDashboard.putNumber("Lift Target Direction", targetDirection);
    }

}

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
    private DigitalInput liftMin, liftMax;
    private int posMax;
    private boolean prevState = false;
    private double startTime = 0.0;
    private double speed;
    private int direction;
    private int pos;
    private int target;
    private boolean manual;
    private int targetDirection;
    private boolean stopThread = true;

    /**
     * Robot's lift mechanism. It's the big elevator-like part that picks up boxes and stuff.
     *
     * @param liftMotorPort  Port for the main lift motor
     * @param liftMinPort  Port for the bottom limit switch
     * @param liftMaxPort  Port for the top limit switch
     * @param amount  How many different "stops" there are on the lift
     */

    public Lift(int liftMotorPort, int liftMinPort, int liftMaxPort, int amount) {
        liftMotor = new Victor(liftMotorPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
        posMax = amount * 2 + 2;
    }

    /**
     * Start the lift thread, this shouldn't be run manually.
     */
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

    /**
     * Sets the speed of the lift
     * @param speed  Speed to set lift to
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Position Control

    /**
     * Position (stop) to set the lift to go to
     * @param target  Where to go
     */
    public void setTarget(int target) {
        this.target = target * 2;
    }

    /**
     * Resets the target to the current position (stops it)
     */
    public void resetTarget() {
        target = pos;
    }

    /**
     * Debug Position
     * @return  position currently at
     */
    public int getPos() {
        return pos;
    }

    /**
     * Checks if moving
     * @return  is it moving
     */
    public boolean isBusy() {
        return pos != target;
    }

    // Manual Control

    /**
     * Start using manual control
     * @param manual  Manual or not
     */
    public void setManual(boolean manual) {
        this.manual = manual;
    }

    /**
     * Where to move to get to target
     * @param targetDirection  Which direction
     */
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
        if (stopThread) {
            stopThread = false;
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        stopThread = true;
    }

    // Check Loop

    private void check() {
        int tempDirection = direction;
        boolean currentState = false;
        // Position Check
        if (currentState != prevState && Timer.getFPGATimestamp() - startTime > SWITCHER_DELAY) {
            pos += tempDirection;
            startTime = Timer.getFPGATimestamp();
        }
        // Manual Check
        if (manual) {
            tempDirection = targetDirection;
        }
        // Auto Check
        else {
            if (pos > target) {
                tempDirection = -1;
            } else if (pos < target) {
                tempDirection = 1;
            } else {
                tempDirection = 0;
            }
        }
        // Limits Check
        if (liftMin.get()) {
            if (tempDirection == -1) {
                tempDirection = 0;
                pos = 0;
            } else {
                pos = tempDirection;
            }
        }
        if (liftMax.get()) {
            if (tempDirection == 1) {
                tempDirection = 0;
                pos = posMax;
            } else {
                pos = posMax + tempDirection;
            }
        }
        direction = tempDirection;
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

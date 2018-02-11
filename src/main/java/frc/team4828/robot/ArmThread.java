package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

public class ArmThread implements Runnable {

    private Talon armMotor;
    private DigitalInput armMin, armMax;

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

    public ArmThread(Talon armMotor, DigitalInput armMin, DigitalInput armMax) {
        this.armMotor = armMotor;
        this.armMin = armMin;
        this.armMax = armMax;
    }

    public void run() {
        System.out.println("Arm Thread Started");
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
        System.out.println("Arm Thread Stopped");
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
        if ((armMin.get() && direction == -1) || (armMax.get() && direction == 1)) {
            setArm(0);
            pos += direction;
        }
        if (pos > target && direction != -1 && !armMin.get()) {
            setArm(-1);
        } else if (pos < target && direction != 1 && !armMax.get()) {
            setArm(1);
        }
    }

    private void setArm(int direction) {
        if (speed != -1) {
            armMotor.set(speed * direction);
        } else {
            armMotor.set(DEFAULT_SPEED * direction);
        }
        this.direction = direction;
    }

    public double getArm() {
        return armMotor.get();
    }

    public boolean isIdle() {
        return pos == target;
    }

}
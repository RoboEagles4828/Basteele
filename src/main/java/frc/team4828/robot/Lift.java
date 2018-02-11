package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift {

    DigitalInput armMin, armMax, liftMax, liftMin, hallEffect;
    Talon liftMotor, armMotor, leftGrabber, rightGrabber;
    public static final double DEFAULT_SPEED = 0.1;

    // private DigitalInput[] switches;

    private DigitalInput switcher;
    private boolean currentState;
    private boolean prevState;

    private int pos = 0;
    private int target = 0;
    private boolean abort = false;
    private int checkDelay = 10;
    private int abortDelay = 100;
    private int direction = 0;
    Thread liftThread;
    boolean stopThread = false;

    int speed = -1;

    public Lift(int hePort, int liftMotorPort, int armMotorPort, int armMaxPort, int armMinPort, int liftMaxPort,
            int liftMinPort, int leftGrabberPort, int rightGrabberPort) {
        hallEffect = new DigitalInput(hePort);
        armMin = new DigitalInput(armMinPort);
        armMax = new DigitalInput(armMaxPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
        liftMotor = new Talon(liftMotorPort);
        armMotor = new Talon(armMotorPort);
        leftGrabber = new Talon(leftGrabberPort);
        rightGrabber = new Talon(rightGrabberPort);
    }

    public void setPos(int pos) {
        System.out.println("Warning: Position has been manually changed");
        this.pos = pos;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void abort() {
        abort = true;
    }

    public void resume() {
        abort = false;
    }

    public void run() {
        System.out.println("Lift Thread Started");
        while (!stopThread) {
            while (!abort) {
                checkPos();
                try {
                    Thread.sleep(checkDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(abortDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Lift Thread Stopped");
    }

    private void checkPos() {
        currentState = switcher.get();
        if (currentState != prevState) {
            pos += direction;
        }
        if (pos > target) {
            setLift(1);
        } else if (pos < target) {
            setLift(-1);
        } else {
            setLift(0);
        }
    }

    private void setLift(int direction) {
        if (speed != -1) {
            liftMotor.set(speed * direction);
        } else {
            liftMotor.set(DEFAULT_SPEED * direction);
        }
        this.direction = direction;
    }

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

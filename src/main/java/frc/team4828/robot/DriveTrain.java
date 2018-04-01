package frc.team4828.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    private Gearbox left, right;
    private AHRS navx;
    private DoubleSolenoid shifter;

    private static final double ENC_RATIO = 25.46; // [ NU / Inch ] => [ NU / Rotations / 6π ]
    private static final double TIMEOUT = 10;

    // MoveDistance Constants
    private static final double MOVE_ANGLE_FACTOR = 0.05;
    private static final double MOVE_RAMP_FACTOR = 0.01;
    private static final double MOVE_ANGLE_THRESH = 0.1;
    private static final double MOVE_ENC_THRESH = 20;
    private static final double MOVE_CHECK_DELAY = 0.001;
    private static final double MOVE_MIN_SPEED = 0.1;

    // TurnDegrees Constants
    private static final double TURN_FACTOR = 0.01;
    private static final double TURN_ANGLE_THRESH = 0.5;
    private static final double TURN_CHECK_DELAY = 0.001;
    private static final double TURN_MIN_SPEED = 0.05;

    // Auton
    private static final double[] MOVE = { 0.5, 0.7 };
    private static final double TURN = 0.3;
    private static final double LENGTH = 38;
    private static final double WIDTH = 34;
    private static final double SWITCH_INIT = 12;
    private static final double SWITCH_OUTER = 55.5 - WIDTH;
    private static final double SWITCH_INNER = 6;
    private static final double SCALE_OUTER = 42 - WIDTH;
    private static final double SCALE_INNER = 12;
    private static final double[] SCALE_OFFSET = { 6, 16 };

    /**
     * DriveTrain for the robot.
     * 
     * @param leftPorts Ports of the left gearbox.
     * @param rightPorts Ports of the right gearbox.
     * @param shifterPorts Ports of the gear shifter solenoid.
     */
    public DriveTrain(int[] leftPorts, int[] rightPorts, int[] shifterPorts) {
        left = new Gearbox(leftPorts[0], leftPorts[1], true);
        right = new Gearbox(rightPorts[0], rightPorts[1], false);
        navx = new AHRS(SPI.Port.kMXP);
        shifter = new DoubleSolenoid(shifterPorts[0], shifterPorts[1]);
        navx.reset();
    }

    /**
     * Drives both gearboxes at a given speed.
     * 
     * @param speed The speed.
     */
    public void drive(double speed) {
        left.drive(speed);
        right.drive(speed);
    }

    /**
     * Turns in place at a given speed.
     * 
     * @param speed The speed.
     */
    public void turn(double speed) {
        left.drive(speed);
        right.drive(-speed);
    }

    /**
     * Stops driving.
     */
    public void brake() {
        drive(0);
    }

    /**
     * Finds the maximum value of the given inputs.
     * 
     * @param input An array of doubles that is to be read.
     * @return Absolute maximum value.
     */
    private double getMaxAbs(double[] input) {
        double max = 0;
        for (double d : input) {
            max = Math.max(max, Math.abs(d));
        }
        return max;
    }

    /**
     * Scales input so that it does not exceed a given maximum.
     * 
     * @param input A double that is to be normalized.
     * @param max Absolute maximum value of output.
     * @return Normalized value.
     */
    private double normalizeAbs(double input, double factor, double max) {
        input *= factor;
        if (Math.abs(input) > Math.abs(max)) {
            input = Math.abs(max) * Math.signum(input);
        }
        return input;
    }

    private double normalizeAbs(double input, double factor, double min, double max) {
        input *= factor;
        if (Math.abs(input) < Math.abs(min)) {
            input = Math.abs(min) * Math.signum(input);
        } else if (Math.abs(input) > Math.abs(max)) {
            input = Math.abs(max) * Math.signum(input);
        }
        return input;
    }

//    private double normalizeAbs(double input, double factor, double max) {
//        return 2 * max / (1 + Math.pow(Math.E, (-factor * input))) - max;
//    }

    /**
     * Scales inputs so that they do not exceed a given maximum.
     *
     * @param input An array of doubles that is to be normalized.
     * @param max Absolute maximum value of output.
     * @return Normalized array.
     */
    private double[] normalizeAbs(double[] input, double max) {
        double inputMax = getMaxAbs(input);
        if (inputMax > max) {
            for (int i = 0; i < input.length; i++) {
                input[i] *= max / inputMax;
            }
        }
        return input;
    }

    /**
     * Drives the left and right gearboxes at speeds determined by the inputs.
     * <p>
     *
     * @param x The x component (Positive is right; Negative is left).
     * @param y The y component (Positive is up; Negative is down).
     * @param angle The angle (Positive is clockwise; Negative is counterclockwise).
     */
    public void arcadeDrive(double x, double y, double angle) {
        angle /= 2;
        double[] drive = new double[2];
        if (x > 0) {
            drive[0] = y + x + angle;
            drive[1] = y - angle;
        } else {
            drive[0] = y + angle;
            drive[1] = y - x - angle;
        }
        drive = normalizeAbs(drive, 1);
        left.drive(drive[0]);
        right.drive(drive[1]);
    }

    /**
     * Moves a given distance forward.
     *
     * @param distance The distance in inches.
     * @param maxSpeed The max speed.
     */
    public void moveDistance(double distance, double angle, double maxSpeed) {
        // Start values
        double startTime = Timer.getFPGATimestamp();
        double startEncL = left.getEnc();
        double startEncR = right.getEnc();
        // Current values
        double currentAngle;
        double currentEnc;
        // Target values
        double targetEnc = distance * ENC_RATIO;

        maxSpeed = Math.abs(maxSpeed) * Math.signum(distance); // Ensure max speed has the right sign
        double speed = maxSpeed; // Set current speed to max

        debugEnc("Begin MOVE");
        debugNavx("Begin MOVE");

        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) { // Loop until break or timeout
            currentAngle = angle - navx.getAngle();
            currentEnc = targetEnc - (left.getEnc() - startEncL + right.getEnc() - startEncR) / 2;
            // Correct angle
            if (Math.abs(currentAngle) > MOVE_ANGLE_THRESH) {
                currentAngle = normalizeAbs(currentAngle, MOVE_ANGLE_FACTOR, speed);
                if (speed * currentAngle > 0) {
                    left.drive(speed);
                    right.drive(speed - currentAngle);
                } else {
                    left.drive(speed + currentAngle);
                    right.drive(speed);
                }
            } else {
                drive(speed);
            }
            // Check encoder
            if (Math.abs(currentEnc) > MOVE_ENC_THRESH) {
                speed = normalizeAbs(currentEnc, MOVE_RAMP_FACTOR, MOVE_MIN_SPEED, maxSpeed);
            } else {
                break;
            }

            Timer.delay(MOVE_CHECK_DELAY);
        }
        brake();
        debugEnc("End MOVE");
        debugNavx("End MOVE");
    }

    public void moveDistance(double distance, double maxSpeed) {
        moveDistance(distance, navx.getAngle(), maxSpeed);
    }

    /**
     * Turns until it faces a given direction.
     * 
     * @param angle The angle in degrees (Determined by the navx).
     * @param speed The speed.
     */
    public void turnDegAbs(double angle, double speed) {
        // Start values
        double startTime = Timer.getFPGATimestamp();
        // Current values
        double currentAngle;

        debugEnc("Begin TURN");
        debugNavx("Begin TURN");

        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) { // Loop until break or timeout
            currentAngle = angle - navx.getAngle();
            // Check angle
            if (Math.abs(currentAngle) > TURN_ANGLE_THRESH) {
                currentAngle = normalizeAbs(currentAngle, TURN_FACTOR, TURN_MIN_SPEED, speed);
                turn(currentAngle);
            } else {
                break;
            }

            Timer.delay(TURN_CHECK_DELAY);
        }
        brake();
        debugEnc("End TURN");
        debugNavx("End TURN");
    }

    public void switchAuton(int init, int target, Dumper dumper) {
        if (init == target) {
            moveDistance(-140, MOVE[1]);
            turnDegAbs(-target * 90, TURN);
        } else if (init == -target) {
            moveDistance(-SWITCH_INIT, MOVE[1]);
            turnDegAbs(init * 90, TURN);
            moveDistance(144 + 2 * SWITCH_OUTER + WIDTH, MOVE[1]);
            turnDegAbs(0, TURN);
            moveDistance(SWITCH_INIT - 140, MOVE[1]);
            turnDegAbs(-target * 90, TURN);
        } else {
            moveDistance(-SWITCH_INIT, MOVE[1]);
            if (target == -1) {
                turnDegAbs(90, TURN);
                moveDistance(72 + 2 * SWITCH_INNER + WIDTH, MOVE[1]);
                turnDegAbs(0, TURN);
            }
            moveDistance(SWITCH_INIT + LENGTH - 130, MOVE[1]);
        }
        drive(-MOVE[0]);
        Timer.delay(1);
        brake();
        dumper.open();
        Timer.delay(0.5);
        dumper.set(DoubleSolenoid.Value.kForward);
    }

    public void scaleAuton(int init, int target, boolean second, Lift lift, Grabber grabber) {
        moveDistance(12, MOVE[1]);
        Timer.delay(0.2);
        lift.setDirection(1);
        moveDistance(210, MOVE[1]);
        turnDegAbs(-init * 90, TURN);
        if (init == target) {
            moveDistance(36 + SCALE_OUTER - SCALE_INNER, MOVE[1]);
        } else if (init == -target) {
            moveDistance(144 + SCALE_OUTER + WIDTH + SCALE_INNER, MOVE[1]);
        }
        turnDegAbs(0, TURN);
        moveDistance(78 - LENGTH - SCALE_OFFSET[1], MOVE[1]);
        while (lift.isBusy()) {
            Timer.delay(0.1);
        }
        moveDistance(SCALE_OFFSET[1] - SCALE_OFFSET[0], MOVE[0]);
        grabber.set(DoubleSolenoid.Value.kForward);
        if (second) {
            Timer.delay(0.5);
            moveDistance(SCALE_OFFSET[0] - SCALE_OFFSET[1], MOVE[0]);
            grabber.stop();
            lift.setDirection(-1);
            turnDegAbs(180, TURN);
            moveDistance(80 - SCALE_OFFSET[1] - LENGTH, MOVE[0]);
            while (lift.isBusy()) {
                Timer.delay(0.1);
            }
            grabber.set(DoubleSolenoid.Value.kForward);
            grabber.intake();
            moveDistance(22, MOVE[0]);
            grabber.set(DoubleSolenoid.Value.kReverse);
            Timer.delay(0.5);
            grabber.stop();
            lift.setDirection(1);
            turnDegAbs(0, TURN);
            moveDistance(102 - SCALE_OFFSET[1] - LENGTH, MOVE[0]);
            while (lift.isBusy()) {
                Timer.delay(0.1);
            }
            moveDistance(SCALE_OFFSET[1] - SCALE_OFFSET[0], MOVE[0]);
            grabber.up();
            grabber.outtake();
        }
    }

    /**
     * Resets the encoders to zero.
     */
    public void zeroEnc() {
        left.zeroEnc();
        right.zeroEnc();
    }

    /**
     * Prints the current values of the encoders.
     */
    public void debugEnc(String input) {
        System.out.println(input + " - Left: " + left.getEnc() + " Right: " + right.getEnc());
    }

    /**
     * Resets the navx.
     */
    public void reset() {
        navx.reset();
    }

    public void debugNavx(String input) {
        System.out.println(input + " - Angle: " + navx.getAngle());
    }

    /**
     * Sets the gear shifter to a given value.
     * 
     * @param mode The solenoid value.
     */
    public void setGear(Value mode) {
        shifter.set(mode);
    }

    /**
     * Updates DriveTrain values on SmartDashboard.
     */
    public void updateDashboard() {
        double[] speeds = { left.get(), right.get() };
        SmartDashboard.putNumberArray("Drive", speeds);
        SmartDashboard.putNumber("Angle", navx.getAngle());
        SmartDashboard.putNumber("Left Encoder", left.get());
        SmartDashboard.putNumber("Right Encoder", right.get());
    }
}

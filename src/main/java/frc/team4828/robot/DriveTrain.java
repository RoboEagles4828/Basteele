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

    // MoveDistance Constants
    private static final double ENC_RATIO = 25.464; // [ NU / Inch ] => [ NU / Rotations / 6π ]
    private static final double CORRECTION_FACTOR = 0.05;
    private static final double ANGLE_THRESH_MOVE = 0.1;
    private static final double ANGLE_CHECK_DELAY = 0.01;
    private static final double TIMEOUT = 10;

    // TurnDegrees Constants
    private static final double TURN_FACTOR = 0.01;
    private static final double ANGLE_THRESH_TURN = 0.1;

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
    private double normalizeAbs(double input, double max) {
        if (Math.abs(input) > Math.abs(max)) {
            input *= Math.abs(max) / Math.abs(input);
        }
        return input;
    }

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
     * @param speed The speed.
     */
    public void moveDistance(double distance, double speed) {
        double startTime = Timer.getFPGATimestamp();
        double startEncL = left.getEnc();
        double startEncR = right.getEnc();
        double startAngle = navx.getAngle();
        double maxEnc = Math.abs(distance * ENC_RATIO);
        double changeAngle;
        if (distance < 0) {
            speed *= -1;
        }
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            changeAngle = startAngle - navx.getAngle();
            if (Math.abs(changeAngle) > ANGLE_THRESH_MOVE) {
                changeAngle = normalizeAbs(changeAngle * CORRECTION_FACTOR, speed);
                if (speed * changeAngle > 0) {
                    right.drive(speed - changeAngle);
                } else {
                    left.drive(speed + changeAngle);
                }
            } else {
                drive(speed);
            }
            if (Math.abs(left.getEnc() - startEncL) >= maxEnc && Math.abs(right.getEnc() - startEncR) >= maxEnc) {
                brake();
                break;
            }
            Timer.delay(ANGLE_CHECK_DELAY);
        }
    }

    /**
     * Turns until it faces a given direction.
     * 
     * @param angle The angle in degrees (Determined by the navx).
     * @param speed The speed.
     */
    public void turnDegAbs(double angle, double speed) {
        double startTime = Timer.getFPGATimestamp();
        double changeAngle;
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            changeAngle = angle - navx.getAngle();
            if (Math.abs(changeAngle) > ANGLE_THRESH_TURN) {
                changeAngle = normalizeAbs(changeAngle * TURN_FACTOR, speed);
                turn(changeAngle);
            } else {
                brake();
                break;
            }
            Timer.delay(ANGLE_CHECK_DELAY);
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
    public void debugEnc() {
        System.out.println("Left: " + left.getEnc() + " Right: " + right.getEnc());
    }

    /**
     * Resets the navx.
     */
    public void reset() {
        navx.reset();
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
        double[] enc = { left.getEnc(), right.getEnc() };
        SmartDashboard.putNumberArray("Drive", speeds);
        SmartDashboard.putNumber("Angle", navx.getAngle());
        SmartDashboard.putNumberArray("Encoders", enc);
    }
}

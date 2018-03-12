package frc.team4828.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    private Gearbox left, right;
    private AHRS navx;
    private DoubleSolenoid shifter;

    // MoveDistance Constants
    private static final double ENC_RATIO = 25.464; // [ NU / Inch ] => [ NU / Rotations / 6π ]
    private static final double CORRECTION_FACTOR = 0.05;
    private static final double ANGLE_THRESH = 0.1;
    private static final double ANGLE_CHECK_DELAY = 0.01;
    private static final double TIMEOUT = 10;

    // TurnDegrees Constants
    private static final double TURN_FACTOR = 0.01;

    /**
     * DriveTrain for the Robot. Takes in a left and right Gearbox and uses arcade drive.
     * <p>
     * Note: Takes in Gearbox Object, not the port.
     */
    public DriveTrain(int[] leftPorts, int[] rightPorts, int[] shifterPorts) {
        left = new Gearbox(leftPorts[0], leftPorts[1], true);
        right = new Gearbox(rightPorts[0], rightPorts[1], false);
        navx = new AHRS(SerialPort.Port.kMXP);
        shifter = new DoubleSolenoid(shifterPorts[0], shifterPorts[1]);
        navx.reset();
    }

    /**
     * Scales inputs so that they remain within 1.
     * <p>
     *
     * @param input An input array of doubles that is to be normalized.
     * @return A same sized array that is normalized.
     */
    private double[] normalize(double[] input) {
        double max = 0;
        for (double anInput : input) {
            double hold = Math.abs(anInput);
            if (hold > max) {
                max = hold;
            }
        }
        if (max > 1) {
            for (int i = 0; i < input.length; i++) {
                input[i] /= max;
            }
        }
        return input;
    }

    /**
     * Takes in x, y, and an angle to produce speeds for left and right gearboxes.
     * <p>
     *
     * @param x     The x component to drive (Positive is right; Negative is left).
     * @param y     The y component to drive (Positive is up; Negative is down).
     * @param angle The angle to drive (Positive is clockwise; Negative is counterclockwise).
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
        drive = normalize(drive);
        left.drive(drive[0]);
        right.drive(drive[1]);
    }

    public void drive(double speed) {
        left.drive(speed);
        right.drive(speed);
    }

    public void brake() {
        left.brake();
        right.brake();
    }

    public void adjustSpeed(double speed, double change) {
        if (Math.abs(change) > Math.abs(speed)) {
            change *= Math.abs(speed) / Math.abs(change);
        }
        if (speed * change > 0) {
            right.drive(speed - change);
        } else {
            left.drive(speed + change);
        }
    }

    public void adjustTurnSpeed(double speedMax, double speed) {
        if (Math.abs(speed) > speedMax) {
            speed *= speedMax / Math.abs(speed);
        }
        left.drive(speed);
        right.drive(-speed);
    }

    /**
     * Moves a certain distance forward. Distance is in meters.
     *
     * @param distance The distance in inches
     * @param speed    The motors' speed
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
            if (Math.abs(changeAngle) > ANGLE_THRESH) {
                adjustSpeed(speed, changeAngle * CORRECTION_FACTOR);
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

    public void turnDegAbs(double angle, double speed) {
        double startTime = Timer.getFPGATimestamp();
        double changeAngle;
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            changeAngle = angle - navx.getAngle();
            if (Math.abs(changeAngle) > ANGLE_THRESH) {
                adjustTurnSpeed(speed, changeAngle * TURN_FACTOR);
            } else {
                brake();
                break;
            }
            Timer.delay(ANGLE_CHECK_DELAY);
        }
    }

    public void zeroEnc() {
        left.zeroEnc();
        right.zeroEnc();
    }

    public void reset() {
        navx.reset();
    }

    public void debugEnc() {
        System.out.println("Left: " + left.getEnc() + " Right: " + right.getEnc());
    }

    public void gearSwitch(Value mode) {
        shifter.set(mode);
    }

    public void updateDashboard() {
        double[] speeds = { left.get(), right.get() };
        double[] enc = { left.getEnc(), right.getEnc() };
        SmartDashboard.putNumberArray("Drive", speeds);
        SmartDashboard.putNumber("Angle", navx.getAngle());
        SmartDashboard.putNumberArray("Encoders", enc);
    }
}

package frc.team4828.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

    private static final double ENC_RATIO = 0.0393;
    private static final double ANGLE_CHECK_DELAY = .1;
    private static final double TIMEOUT = 10;
    private static final double P = 0.2;

    private Gearbox left, right;
    private AHRS navx;
    private DoubleSolenoid shifter;

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
        double changeEncL, changeEncR;
        double startAngle = navx.getAngle();
        double maxEnc = Math.abs(distance * ENC_RATIO);
        if (distance < 0) {
            speed *= -1;
        }
        while (Timer.getFPGATimestamp() - startTime < TIMEOUT) {
            left.drive(speed);
            right.drive(speed); // TODO: Fix PID and Check why left doesnt move.
//            while ((navx.getAngle() - startAngle > 0) && (Timer.getFPGATimestamp() - startTime < TIMEOUT)) {
//                left.change((startAngle - navx.getAngle()) * P);
//                Timer.delay(ANGLE_CHECK_DELAY);
//            }
//            while ((navx.getAngle() - startAngle < 0) && (Timer.getFPGATimestamp() - startTime < TIMEOUT)) {
//                right.change((navx.getAngle() - startAngle) * P);
//                Timer.delay(ANGLE_CHECK_DELAY);
//            }
            changeEncL = Math.abs(left.getEnc() - startEncL);
            changeEncR = Math.abs(right.getEnc() - startEncR);
            SmartDashboard.putNumber("Current L Enc", changeEncL);
            SmartDashboard.putNumber("Current R Enc", changeEncR);

            if (changeEncL >= maxEnc && changeEncR >= maxEnc) {
                left.brake();
                right.brake();
                break;
            }
        }
    }

    public void turnDegAbs(double angle, double speed) {
        double start = navx.getAngle();
        if (start < angle) {
            left.drive(speed);
            right.drive(-speed);
            while (navx.getAngle() < angle) {
                SmartDashboard.putNumber("Angle", navx.getAngle());
                Timer.delay(ANGLE_CHECK_DELAY);
            }
        } else {
            left.drive(-speed);
            right.drive(speed);
            while (navx.getAngle() > angle) {
                SmartDashboard.putNumber("Angle", navx.getAngle());
                Timer.delay(ANGLE_CHECK_DELAY);
            }
        }
        left.brake();
        right.brake();
    }

    public void zeroEnc() {
        left.zeroEnc();
        right.zeroEnc();
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

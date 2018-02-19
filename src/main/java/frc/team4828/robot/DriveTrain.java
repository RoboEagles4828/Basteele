package frc.team4828.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class DriveTrain {

    private static final double ENC_RATIO = 0.03952;
    private static final double ANGLE_CHECK_DELAY = .1;

    Gearbox left, right;
    AHRS navx;

    /**
     * DriveTrain for the Robot. Takes in a left and right Gearbox and uses arcade drive.
     * <p>
     * Note: Takes in Gearbox Object, not the port.
     *
     * @param left   Left Gearbox.
     * @param right  Right Gearbox.
     */
    public DriveTrain(Gearbox left, Gearbox right) {
        this.left = left;
        this.right = right;
        navx = new AHRS(SerialPort.Port.kMXP);
    }

    /**
     * Scales inputs so that they remain within 1.
     * <p>
     *
     * @param input  An input array of doubles that is to be normalized.
     * @return       A same sized array that is normalized.
     */
    public double[] normalize(double[] input) {
        double max = 0;
        for (int i = 0; i < input.length; i++) {
            double hold = Math.abs(input[i]);
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
     * @param x      The x component to drive (Positive is right; Negative is left).
     * @param y      The y component to drive (Positive is up; Negative is down).
     * @param angle  The angle to drive (Positive is counterclockwise; Negative is clockwise).
     */
    public void arcadeDrive(double x, double y, double angle) {

        double[] drive = new double[2];
        if (x > 0) {
            drive[0] = y + x - angle;
            drive[1] = y + angle;
        } else {
            drive[0] = y - angle;
            drive[1] = y - x + angle;
        }
        drive = normalize(drive);
        left.drive(drive[0]);
        right.drive(drive[1]);
    }

    /**
     * Moves a certain distance forward. Distance is in meters.
     *
     * @param distance  The distance in feet
     * @param speed     The motors' speed
     */
    public void moveDistance(double distance, double speed) {
        double startEncL = getLeftEnc();
        double startEncR = getRightEnc();
        double changeEncL = 0;
        double changeEncR = 0;
        double maxEnc = Math.abs(distance * ENC_RATIO);
        if (distance > 0) {
            left.drive(speed);
            right.drive(speed);
        } else {
            left.drive(-speed);
            right.drive(-speed);
        }
        while (changeEncL < maxEnc || changeEncR < maxEnc) {
            changeEncL = Math.abs(getLeftEnc() - startEncL);
            changeEncR = Math.abs(getRightEnc() - startEncR);
            if (changeEncL >= maxEnc) {
                left.brake();
            }
            if (changeEncR >= maxEnc) {
                right.brake();
            }
        }
    }

    public void turnDegAbs(double angle, double speed) {
        double start = navx.getAngle();
        if (start > angle) {
            left.drive(speed);
            right.drive(-speed);
            while (navx.getAngle() > angle) {
                Timer.delay(ANGLE_CHECK_DELAY);
            }
        } else {
            left.drive(-speed);
            right.drive(speed);
            while (navx.getAngle() < angle) {
                Timer.delay(ANGLE_CHECK_DELAY);
            }
        }
        left.brake();
        right.brake();
    }

    public void turnDegRel(double angle, double speed) {
        turnDegAbs(navx.getAngle() + angle, speed);
    }

    public void zeroEnc() {
        left.zeroEnc();
        right.zeroEnc();
    }

    public double getLeftEnc() {
        return -left.getEnc();
    }

    public double getRightEnc() {
        return right.getEnc();
    }

    public void debugEnc() {
        System.out.println("Left: " + getLeftEnc() + " Right: " + getRightEnc());
    }

    public void brake() {
        left.brake();
        right.brake();
    }

}

package frc.team4828.robot;

public class DriveTrain {

    private static final double TWIST_THRESH = .3;
    private static final double TWIST_FACTOR = .5;
    Gearbox left, right;

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
     * Changes the inputs from the joystick to work with arcadeDrive.
     * <p>
     *
     * @param x      The x component of the joystick.
     * @param y      The y component of the joystick.
     * @param twist  The twist component of the joystick.
     */
    public void jArcadeDrive(double x, double y, double twist) {
        if (Math.abs(twist) < TWIST_THRESH) {
            twist = 0;
        } else {
            if (twist > 0) {
                twist -= TWIST_THRESH;
            } else {
                twist += TWIST_THRESH;
            }
        }
        y *= -1;
        twist *= -1;
        twist *= TWIST_FACTOR;
        arcadeDrive(x, y, twist);
    }

    /**
     * Prints debug info to the console.
     * <p>
     *
     * @param x      X to print.
     * @param y      Y to print.
     * @param twist  Twist to print.
     */
    public void debug(double x, double y, double twist) {
        System.out.println("X: " + x + " Y: " + y + " Twist: " + twist);
    }

}

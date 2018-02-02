package frc.team4828.robot;

public class DriveTrain {

    private static final double TWIST_THRESH = .3;
    Gearbox left, right;

    public DriveTrain(Gearbox left, Gearbox right) {
        this.left = left;
        this.right = right;
    }

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
     * Arcade Drive
     * 
     * @param x
     *            The x component to drive (Positive is right; Negative is left)
     * @param y
     *            The y component to drive (Positive is up; Negative is down)
     * @param twist
     *            The angle to drive
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
        arcadeDrive(x, y, twist);
    }

    public void debug(double x, double y, double twist) {
        System.out.println("X: " + x + " Y: " + y + " Twist: " + twist);
    }

}

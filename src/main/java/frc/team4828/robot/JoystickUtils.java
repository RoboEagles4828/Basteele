package frc.team4828.robot;

public class JoystickUtils {

    private static final double THRESHOLD = 0.01;
    private static final double ANGLE_THRESHOLD = 0.1;

    public static double processX(double x) {
        if (Math.abs(x) < THRESHOLD) {
            x = 0;
        } else {
            if (x > 0) {
                x -= THRESHOLD;
            } else {
                x += THRESHOLD;
            }
        }
        return x;
    }

    public static double processY(double y) {
        processX(y);
        y *= -1;
        return y;
    }

    public static double processTwist(double twist) {
        if (Math.abs(twist) < ANGLE_THRESHOLD) {
            twist = 0;
        } else {
            if (twist > 0) {
                twist -= ANGLE_THRESHOLD;
            } else {
                twist += ANGLE_THRESHOLD;
            }
        }
        twist *= -1;
        return twist;
    }

    /**
     * Prints debug info to the console.
     * <p>
     *
     * @param x      X to print.
     * @param y      Y to print.
     * @param twist  Twist to print.
     */
    public static void debug(double x, double y, double twist) {
        System.out.println("X: " + x + " Y: " + y + " Twist: " + twist);
    }

}

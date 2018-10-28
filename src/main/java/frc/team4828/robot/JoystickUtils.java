package frc.team4828.robot;

public class JoystickUtils {

    private static final double THRESHOLD = 0.1;
    private static final double ANGLE_THRESHOLD = 0.1;

    /**
     * Force X to be above 0 and the threshold
     * @param x  number to change
     * @return  changed number
     */
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

    /**
     * Make Y negative
     * @param y  the y
     * @return  negatived
     */
    public static double processY(double y) {
        y *= -1;
        return y;
    }

    /**
     * Make twist make more sense
     * @param twist  The default twist value
     * @return  the good twist value
     */
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
        return twist;
    }
}

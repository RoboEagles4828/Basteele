package frc.team4828.robot;

public class Buttons {
    /**
     * Class that holds all the buttons for all the actions. This class should never be instantiated.
     */

    // Drive Joystick

    public static final int DUMPER_ON = 11;
    public static final int DUMPER_OFF = 12;

    public static final int CLIMB_UP = 9;
    public static final int CLIMB_DOWN = 10;

    // Lift Joystick

    public static final int[] LIFT = { 11, 12, 9, 10, 7 };

    public static final int LIFT_MANUAL = 8;
    public static final int LIFT_UP = 9;
    public static final int LIFT_DOWN = 10;

    public static final int GRABBER_OPEN = 5;
    public static final int GRABBER_CLOSE = 3;
    public static final int GRABBER_IN = 1;
    public static final int GRABBER_OUT = 2;

    public static final int ARM_UP = 6;
    public static final int ARM_DOWN = 4;

    public Buttons() {
        System.out.println("Don't instantiate the Buttons class, fool!");
    }
}

package frc.team4828.robot;

public class Buttons {
    /**
     * Class that holds all the buttons for all the actions. This class should never be instantiated.
     */

    public static final int DUMPER_ON = 11;
    public static final int DUMPER_OFF = 12;
    public static final int[] LIFT = { 7, 8, 9, 10, 2 };

    public static final int LIFT_UP = 9;
    public static final int LIFT_DOWN = 10;

    public static final int CLIMB_UP = 13;
    public static final int CLIMB_DOWN = 14;

    public static final int GRABBER_OPEN = 5;
    public static final int GRABBER_CLOSE = 6;
    public static final int GRABBER_IN = 3;
    public static final int GRABBER_OUT = 4;

    public Buttons() {
        System.out.println("Don't instantiate the Buttons class, fool!");
    }
}

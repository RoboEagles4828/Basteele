package frc.team4828.robot;

public class Buttons {
    /**
     * Class that holds all the buttons for all the actions. This class should never be instantiated.
     */

    public static final int DUMPER_ON = 11;
    public static final int DUMPER_OFF = 12;
    public static final int[] LIFT = { 7, 8, 9, 10, 1 };

    public static final int LIFT_UP = 9;
    public static final int LIFT_DOWN = 10;

    public static final int CLIMB_UP = 7;
    public static final int CLIMB_DOWN = 8;

    public static final int[] GRABBER_SOL = { 5, 6 };
    public static final int[] GRABBER_WHEELS = { 3, 4 };

    public Buttons() {
        System.out.println("Don't instantiate the Buttons class, fool!");
    }
}

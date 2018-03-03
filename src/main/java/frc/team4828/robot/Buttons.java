package frc.team4828.robot;

public class Buttons {
    /**
     * Class that holds all the buttons for all the actions. This class should never be instantiated.
     */

    // Drive Joystick
    public static final int DUMPER = 1;

    public static final int CLIMB_UP = 8;
    public static final int CLIMB_DOWN = 7;

    public static final int[] GEAR_SWITCH = { 3, 4 };

    // Lift Joystick
    public static final int[] LIFT = { 7, 12, 9, 10, 8 }; // TODO: Make it so that scale is one option that is just one magnet. Switch to manual after going here

    public static final int GRABBER_OPEN = 2;
    public static final int[] GRABBER_CLOSE = { 3, 4, 5, 6 };
    public static final int GRABBER_OUT = 1;

    public Buttons() {
        System.out.println("Don't instantiate the Buttons class, fool!");
    }
}

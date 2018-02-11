package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    private TalonSRX motor1, motor2, motor3, motor4;
    private Compressor comp;
    private DoubleSolenoid sol1, sol2;
    private PneumaticSwitch switcher1, switcher2;
    private Gearbox leftGearbox, rightGearbox;
    private DoubleSolenoid dumperSol;

    private Talon liftMotor, armMotor, leftGrabber, rightGrabber;
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;

    private Joystick joystick;

    private DriveTrain drive;
    private PneumaticSwitch dumper;
    private Lift lift;

    private DigitalInput switch1, switch2, switch3;
    private boolean doneAuton = false;


    public void robotInit() {
        motor1 = new TalonSRX(Ports.LEFT_MOTORS[0]);
        motor2 = new TalonSRX(Ports.LEFT_MOTORS[1]);
        motor3 = new TalonSRX(Ports.RIGHT_MOTORS[0]);
        motor4 = new TalonSRX(Ports.RIGHT_MOTORS[1]);
        comp = new Compressor(Ports.COMPRESSOR);
        comp.setClosedLoopControl(true);
        sol1 = new DoubleSolenoid(Ports.LEFT_SOLENOID[0], Ports.LEFT_SOLENOID[1]);
        sol2 = new DoubleSolenoid(Ports.RIGHT_SOLENOID[0], Ports.RIGHT_SOLENOID[1]);
        switcher1 = new PneumaticSwitch(comp, sol1);
        switcher2 = new PneumaticSwitch(comp, sol2);
        leftGearbox = new Gearbox(motor1, motor2, switcher1);
        rightGearbox = new Gearbox(motor3, motor4, switcher2);
        dumperSol = new DoubleSolenoid(Ports.DUMPER[0], Ports.DUMPER[1]);

        liftMotor = new Talon(Ports.LIFT_MOTOR);
        armMotor = new Talon(Ports.ARM_MOTOR);
        leftGrabber = new Talon(Ports.LEFT_GRABBER);
        rightGrabber = new Talon(Ports.RIGHT_GRABBER);

        liftMin = new DigitalInput(Ports.LIFT_MIN);
        liftMax = new DigitalInput(Ports.LIFT_MAX);
        armMin = new DigitalInput(Ports.ARM_MIN);
        armMax = new DigitalInput(Ports.ARM_MAX);
        switcher = new DigitalInput(Ports.SWITCHER);

        joystick = new Joystick(Ports.JOYSTICK);

        drive = new DriveTrain(leftGearbox, rightGearbox);
        dumper = new PneumaticSwitch(comp, dumperSol);
        lift = new Lift(liftMotor, armMotor, leftGrabber, rightGrabber, liftMin, liftMax, armMin, armMax, switcher);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");

        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
        if(!doneAuton) {
            int mode = 0;
            if (switch1.get())
                mode += 4;
            if (switch2.get())
                mode += 2;
            if (switch3.get())
                mode += 1;
            switch (mode) {
                case 0:
                    // Just go forward
                    drive.moveDistance(120, .5);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                default:
                    break;
            }
            doneAuton = true;
        }
        Timer.delay(.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");

        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        drive.jArcadeDrive(joystick.getX(), joystick.getY(), joystick.getTwist());
        if (joystick.getRawButton(Buttons.DUMPER_ON)) {
            dumper.set(1);
        } else if (joystick.getRawButton(Buttons.DUMPER_OFF)) {
            dumper.set(-1);
        }
        if (joystick.getRawButton(Buttons.LIFT[0])) {
            lift.setLiftPos(0);
        } else if (joystick.getRawButton(Buttons.LIFT[1])) {
            lift.setLiftPos(2);
        } else if (joystick.getRawButton(Buttons.LIFT[2])) {
            lift.setLiftPos(4);
        } else if (joystick.getRawButton(Buttons.LIFT[3])) {
            lift.setLiftPos(6);
        } else if (joystick.getRawButton(Buttons.LIFT[4])) {
            lift.setLiftPos(8);
        }
        Timer.delay(.1);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        double x = joystick.getX();
        double y = joystick.getY();
        double twist = joystick.getTwist();
        drive.debug(x, y, twist);
        Timer.delay(.1);
    }

}

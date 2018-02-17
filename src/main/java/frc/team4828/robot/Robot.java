package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    private TalonSRX motor1, motor2, motor3, motor4;
    private Compressor comp;
    private DoubleSolenoid shifterSol, dumperSol, grabberSol;
    private PneumaticSwitch switcher1, switcher2, grabberSwitcher;
    private Gearbox leftGearbox, rightGearbox;

    private Victor liftMotor, armMotor, leftGrabberMotor, rightGrabberMotor, leftClimberMotor, rightClimberMotor;
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;

    private Grabber grabber;

    private Joystick joystick;

    private DriveTrain drive;
    private PneumaticSwitch dumper;
    private Lift lift;
    private int liftDirection;

    private DigitalInput switch1, switch2, switch3;
    private boolean doneAuton = false;

    public void robotInit() {
        motor1 = new TalonSRX(Ports.LEFT_MOTORS[0]);
        motor2 = new TalonSRX(Ports.LEFT_MOTORS[1]);
        motor3 = new TalonSRX(Ports.RIGHT_MOTORS[0]);
        motor4 = new TalonSRX(Ports.RIGHT_MOTORS[1]);

        comp = new Compressor(Ports.COMPRESSOR);
        comp.setClosedLoopControl(true);

        shifterSol = new DoubleSolenoid(Ports.SHIFTER_SOLENOID[0], Ports.SHIFTER_SOLENOID[1]);
        switcher1 = new PneumaticSwitch(comp, shifterSol);
        switcher2 = new PneumaticSwitch(comp, shifterSol);
        leftGearbox = new Gearbox(motor1, motor2, switcher1);
        rightGearbox = new Gearbox(motor3, motor4, switcher2);

        dumperSol = new DoubleSolenoid(Ports.DUMPER[0], Ports.DUMPER[1]);

        liftMotor = new Victor(Ports.LIFT_MOTOR);
        armMotor = new Victor(Ports.ARM_MOTOR);

        leftGrabberMotor = new Victor(Ports.LEFT_GRABBER);
        rightGrabberMotor = new Victor(Ports.RIGHT_GRABBER);
        grabberSol = new DoubleSolenoid(Ports.GRABBER_SOLENOID[0], Ports.GRABBER_SOLENOID[1]);
        grabberSwitcher = new PneumaticSwitch(comp, grabberSol);

        leftClimberMotor = new Victor(Ports.LEFT_CLIMBER);
        rightClimberMotor = new Victor(Ports.RIGHT_CLIMBER);

        liftMin = new DigitalInput(Ports.LIFT_MIN);
        liftMax = new DigitalInput(Ports.LIFT_MAX);
        armMin = new DigitalInput(Ports.ARM_MIN);
        armMax = new DigitalInput(Ports.ARM_MAX);
        switcher = new DigitalInput(Ports.SWITCHER);

        joystick = new Joystick(Ports.JOYSTICK);

        drive = new DriveTrain(leftGearbox, rightGearbox);
        dumper = new PneumaticSwitch(comp, dumperSol);
        grabber = new Grabber(leftGrabberMotor, rightGrabberMotor, grabberSwitcher);
        lift = new Lift(liftMotor, armMotor, liftMin, liftMax, armMin, armMax, switcher);

        switch1 = new DigitalInput(Ports.AUTON_MODE[0]);
        switch2 = new DigitalInput(Ports.AUTON_MODE[1]);
        switch3 = new DigitalInput(Ports.AUTON_MODE[2]);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");

        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            System.out.println((switch1.get() ? "1" : "0") + (switch2.get() ? "1" : "0") + (switch3.get() ? "1" : "0"));
            int mode = (switch1.get() ? 4 : 0) + (switch2.get() ? 2 : 0) + (switch3.get() ? 1 : 0);
            System.out.println("Auton Mode: " + mode);
            switch (10) { // TODO Change to mode
            case 0:
                // Just go forward
                drive.moveDistance(120, .5);
                break;
            case 1:
                // Start from the left edge, go fwd, turn and outtake
                drive.moveDistance(150, .5);
                drive.turnDegAbs(90, .5);
                lift.setLiftTarget(2);
                while (!lift.isLiftIdle()) {
                    Timer.delay(.1);
                }
                grabber.outtake();
                break;
            case 2:
                // Start from the right edge, go fwd, turn and outtake
                drive.moveDistance(150, .5);
                drive.turnDegAbs(270, .5);
                lift.setLiftTarget(2);
                while (!lift.isLiftIdle()) {
                    Timer.delay(.1);
                }
                grabber.outtake();
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
                // Outtake into the hole
                grabber.outtake();
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
        lift.startLiftThread();
//        lift.setManual(true);
        lift.setLiftSpeed(0.5);
        liftDirection = 0;
        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        if (joystick.getTrigger()) {
            drive.jArcadeDrive(joystick.getX(), joystick.getY(), joystick.getTwist());
        } else {
            drive.brake();
        }
        drive.debug(joystick.getX(), joystick.getY(), joystick.getTwist());
        if (joystick.getRawButton(Buttons.DUMPER_ON)) {
            dumper.set(1);
        } else if (joystick.getRawButton(Buttons.DUMPER_OFF)) {
            dumper.set(-1);
        }
        if (joystick.getRawButton(Buttons.LIFT[0])) {
            lift.setLiftTarget(0);
        } else if (joystick.getRawButton(Buttons.LIFT[1])) {
            lift.setLiftTarget(1);
        } else if (joystick.getRawButton(Buttons.LIFT[2])) {
            lift.setLiftTarget(2);
        } else if (joystick.getRawButton(Buttons.LIFT[3])) {
            lift.setLiftTarget(3);
        } else if (joystick.getRawButton(Buttons.LIFT[4])) {
            lift.setLiftTarget(4);
        }
        if (joystick.getRawButton(Buttons.LIFT_UP)) {
            if (liftDirection != 1) {
                lift.setLiftTargetDirection(1);
                liftDirection = 1;
            }
        } else if (joystick.getRawButton(Buttons.LIFT_DOWN)) {
            if (liftDirection != -1) {
                lift.setLiftTargetDirection(-1);
                liftDirection = -1;
            }
        } else {
            if (liftDirection != 0) {
                lift.setLiftTargetDirection(0);
                liftDirection = 0;
            }
        }
        if (joystick.getRawButton(Buttons.GRABBER_OPEN)) {
            grabber.open();
        } else if (joystick.getRawButton(Buttons.GRABBER_CLOSE)) {
            grabber.close();
        }
        if (joystick.getRawButton(Buttons.GRABBER_IN)) {
            grabber.intake();
        } else if (joystick.getRawButton(Buttons.GRABBER_OUT)) {
            grabber.outtake();
        } else {
            grabber.stop();
        }
        if (joystick.getRawButton(Buttons.CLIMB_UP)) {
            leftClimberMotor.set(.1);
            rightClimberMotor.set(.1);
        } else if (joystick.getRawButton(Buttons.CLIMB_DOWN)) {
            leftClimberMotor.set(-.1);
            rightClimberMotor.set(-.1);
        } else {
            leftClimberMotor.set(0);
            rightClimberMotor.set(0);
        }
        Timer.delay(.01);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        System.out.println(liftMin.get());
    }

    public void disabledInit() {
        lift.stopLiftThread();
    }
}

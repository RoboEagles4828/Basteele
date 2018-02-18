package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
    // Joysticks
    private Joystick driveStick;
    private Joystick liftStick;
    // Motors
    private TalonSRX motor1, motor2, motor3, motor4;
    private Victor liftMotor, armMotor, leftGrabberMotor, rightGrabberMotor, leftClimberMotor, rightClimberMotor;
    // DIO
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;
    // Pneumatics
    private Compressor comp;
    private DoubleSolenoid shifterSol, dumperSol, grabberSol;
    private PneumaticSwitch switcher1, switcher2, grabberSwitcher, dumper;
    // Drive
    private Gearbox leftGearbox, rightGearbox;
    private DriveTrain drive;
    // Lift
    private Lift lift;
    private boolean liftManualPrev;
    private boolean liftManual;
    // Grabber
    private Grabber grabber;
    // Climber
    private Climber climber;
    // Auton
    private DigitalInput switch1, switch2, switch3;
    private boolean doneAuton;

    public void robotInit() {
        // Joysticks
        driveStick = new Joystick(Ports.DRIVE_STICK);
        liftStick = new Joystick(Ports.LIFT_STICK);
        // Motors
        motor1 = new TalonSRX(Ports.LEFT_GEARBOX[0]);
        motor2 = new TalonSRX(Ports.LEFT_GEARBOX[1]);
        motor3 = new TalonSRX(Ports.RIGHT_GEARBOX[0]);
        motor4 = new TalonSRX(Ports.RIGHT_GEARBOX[1]);
        liftMotor = new Victor(Ports.LIFT);
        armMotor = new Victor(Ports.ARM);
        leftGrabberMotor = new Victor(Ports.LEFT_GRABBER);
        rightGrabberMotor = new Victor(Ports.RIGHT_GRABBER);
        leftClimberMotor = new Victor(Ports.LEFT_CLIMBER);
        rightClimberMotor = new Victor(Ports.RIGHT_CLIMBER);
        // DIO
        liftMin = new DigitalInput(Ports.LIFT_MIN);
        liftMax = new DigitalInput(Ports.LIFT_MAX);
        armMin = new DigitalInput(Ports.ARM_MIN);
        armMax = new DigitalInput(Ports.ARM_MAX);
        switcher = new DigitalInput(Ports.SWITCHER);
        // Pneumatics
        comp = new Compressor(Ports.COMPRESSOR);
        shifterSol = new DoubleSolenoid(Ports.SHIFTER[0], Ports.SHIFTER[1]);
        dumperSol = new DoubleSolenoid(Ports.DUMPER[0], Ports.DUMPER[1]);
        grabberSol = new DoubleSolenoid(Ports.GRABBER[0], Ports.GRABBER[1]);
        switcher1 = new PneumaticSwitch(comp, shifterSol);
        switcher2 = new PneumaticSwitch(comp, shifterSol);
        grabberSwitcher = new PneumaticSwitch(comp, grabberSol);
        dumper = new PneumaticSwitch(comp, dumperSol);
        // Drive
        leftGearbox = new Gearbox(motor1, motor2, switcher1);
        rightGearbox = new Gearbox(motor3, motor4, switcher2);
        drive = new DriveTrain(leftGearbox, rightGearbox);
        // Lift
        lift = new Lift(liftMotor, armMotor, liftMin, liftMax, armMin, armMax, switcher);
        // Grabber
        grabber = new Grabber(leftGrabberMotor, rightGrabberMotor, grabberSwitcher);
        // Climber
        climber = new Climber(leftClimberMotor, rightClimberMotor);
        // Auton
        switch1 = new DigitalInput(Ports.AUTON[0]);
        switch2 = new DigitalInput(Ports.AUTON[1]);
        switch3 = new DigitalInput(Ports.AUTON[2]);
        doneAuton = false;

        comp.setClosedLoopControl(true);
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
        lift.startArmThread();
        lift.setLiftSpeed(0.5);
        lift.setArmSpeed(0.5);
        liftManualPrev = false;
        liftManual = false;
        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        // Drive
        if (driveStick.getTrigger()) {
            drive.arcadeDrive(JoystickUtils.processX(driveStick.getX()), JoystickUtils.processY(driveStick.getY()),
                    JoystickUtils.processTwist(driveStick.getTwist()));
        } else {
            drive.brake();
        }
        // Drive Debug
        drive.debug(driveStick.getX(), driveStick.getY(), driveStick.getTwist());
        // Dumper
        if (driveStick.getRawButton(Buttons.DUMPER_ON)) {
            dumper.set(1);
        } else if (driveStick.getRawButton(Buttons.DUMPER_OFF)) {
            dumper.set(-1);
        }
        // Lift Set Manual
        if (liftStick.getRawButton(Buttons.LIFT_MANUAL) && !liftManualPrev) {
            liftManual = !liftManual;
            lift.setManual(liftManual);
        }
        liftManualPrev = liftStick.getRawButton(Buttons.LIFT_MANUAL);
        // Lift
        if (liftManual) {
//            if (liftStick.getRawButton(Buttons.LIFT_UP)) {
//                lift.setLiftTargetDirection(1);
//            } else if (liftStick.getRawButton(Buttons.LIFT_DOWN)) {
//                lift.setLiftTargetDirection(-1);
//            } else {
//                lift.setLiftTargetDirection(0);
//            }
            lift.setLiftSpeedManual(JoystickUtils.processY(liftStick.getY()));
        } else {
            if (liftStick.getRawButton(Buttons.LIFT[0])) {
                lift.setLiftTarget(0);
            } else if (liftStick.getRawButton(Buttons.LIFT[1])) {
                lift.setLiftTarget(2);
            } else if (liftStick.getRawButton(Buttons.LIFT[2])) {
                lift.setLiftTarget(4);
            } else if (liftStick.getRawButton(Buttons.LIFT[3])) {
                lift.setLiftTarget(6);
            } else if (liftStick.getRawButton(Buttons.LIFT[4])) {
                lift.setLiftTarget(8);
            }
        }
        // Arm
        if (liftStick.getRawButton(Buttons.ARM_UP)) {
            lift.setArmTarget(0);
        } else if (liftStick.getRawButton(Buttons.ARM_DOWN)) {
            lift.setArmTarget(1);
        }
        // Grabber
        if (liftStick.getRawButton(Buttons.GRABBER_OPEN)) {
            grabber.open();
        } else if (liftStick.getRawButton(Buttons.GRABBER_CLOSE)) {
            grabber.close();
        }
        if (liftStick.getRawButton(Buttons.GRABBER_IN)) {
            grabber.intake();
        } else if (liftStick.getRawButton(Buttons.GRABBER_OUT)) {
            grabber.outtake();
        } else {
            grabber.stop();
        }
        // Climber
        if (driveStick.getRawButton(Buttons.CLIMB_UP)) {
            climber.up();
        } else if (driveStick.getRawButton(Buttons.CLIMB_DOWN)) {
            climber.down();
        } else {
            climber.stop();
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
        lift.stopArmThread();
    }
}

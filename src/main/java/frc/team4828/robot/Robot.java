package frc.team4828.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
    // Joysticks
    private Joystick driveStick;
    private Joystick liftStick;
    // Drive
    private DriveTrain drive;
    // Lift
    private Lift lift;
    // Grabber
    private Grabber grabber;
    // Climber
    private Climber climber;
    // Auton
    private DigitalInput switch1, switch2, switch3;
    private boolean doneAuton;
    private String data;
    // Dumper
    private Dumper dumper;
    private Compressor comp;

    public void robotInit() {
        // Joysticks
        driveStick = new Joystick(Ports.DRIVE_STICK);
        liftStick = new Joystick(Ports.LIFT_STICK);
        // Pneumatics
        comp = new Compressor(Ports.COMPRESSOR);
        comp.setClosedLoopControl(true);
        // Drive
        drive = new DriveTrain(Ports.LEFT_GEARBOX, Ports.RIGHT_GEARBOX, Ports.SHIFTER);
        // Lift
        lift = new Lift(Ports.LIFT, Ports.LIFT_MIN, Ports.LIFT_MAX, Ports.SWITCHER);
        // Grabber
        grabber = new Grabber(Ports.LEFT_GRABBER, Ports.RIGHT_GRABBER, Ports.GRABBER);
        // Climber
        climber = new Climber(Ports.LEFT_CLIMBER, Ports.RIGHT_CLIMBER);
        // Auton
        switch1 = new DigitalInput(Ports.AUTON[0]);
        switch2 = new DigitalInput(Ports.AUTON[1]);
        switch3 = new DigitalInput(Ports.AUTON[2]);
        doneAuton = false;

        dumper = new Dumper(Ports.DUMPER, Ports.SERVO, Ports.PROX);

        comp.setClosedLoopControl(true);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Autonomous Init ---");
        data = DriverStation.getInstance().getGameSpecificMessage();
        doneAuton = false;
        System.out.println(" --- Start Autonomous ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            SmartDashboard.putString("Auton Mode Raw",
                    (switch1.get() ? "1" : "0") + (switch2.get() ? "1" : "0") + (switch3.get() ? "1" : "0"));
            int mode = (switch1.get() ? 4 : 0) + (switch2.get() ? 2 : 0) + (switch3.get() ? 1 : 0);
            SmartDashboard.putNumber("Autonomous Mode: ", mode);
            switch (1) { // TODO Change to mode
            case 0:
                // Just go forward
                drive.moveDistance(120, .5);
                break;
            case 1:
                // Switch from Left
                // Check left or right side
                switch (data.charAt(0)) {
                case 'L':
                    drive.moveDistance(150, 1);
                    drive.turnDegAbs(-90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(DoubleSolenoid.Value.kForward);
                    break;
                case 'R':
                    drive.moveDistance(5, 1);
                    drive.turnDegAbs(24, .5);
                    drive.moveDistance(288, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(60, 1);
                    drive.turnDegAbs(90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(DoubleSolenoid.Value.kForward);
                    break;
                }
                break;
            case 2:
                // Switch from Right
                // Check left or right side
                switch (data.charAt(0)) {
                case 'L':
                    drive.moveDistance(5, 1);
                    drive.turnDegAbs(-24, .5);
                    drive.moveDistance(288, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(60, 1);
                    drive.turnDegAbs(-90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(DoubleSolenoid.Value.kForward);
                    break;
                case 'R':
                    drive.moveDistance(150, 1);
                    drive.turnDegAbs(90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(DoubleSolenoid.Value.kForward);
                    break;
                }
                break;
            case 3:
                // Scale from Left
                switch (data.charAt(1)) {
                case 'L':
                    drive.moveDistance(310, 1);
                    lift.setTarget(6);
                    while (lift.isNotIdle()) {
                        Timer.delay(0.1);
                    }
                    drive.turnDegAbs(90, 0.5);
                    drive.moveDistance(10, 1);
                    grabber.outtake();
                    break;
                case 'R':
                    drive.moveDistance(5, 1);
                    drive.turnDegAbs(24, .5);
                    drive.moveDistance(288, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(190, 1);
                    lift.setTarget(6);
                    while (lift.isNotIdle()) {
                        Timer.delay(0.1);
                    }
                    drive.turnDegAbs(270, 0.5);
                    drive.moveDistance(10, 1);
                    grabber.outtake();
                    break;
                }
                break;
            case 4:
                // Scale from Right
                switch (data.charAt(0)) {
                case 'L':
                    drive.moveDistance(5, 1); //TODO: Lower Speed
                    drive.turnDegAbs(-24, .5);
                    drive.moveDistance(300, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(190, 1);
                    lift.setTarget(6);
                    while (lift.isNotIdle()) {
                        Timer.delay(0.1);
                    }
                    drive.turnDegAbs(90, .5);
                    drive.moveDistance(10, 1);
                    grabber.outtake();
                    break;
                case 'R':
                    drive.moveDistance(310, 1);
                    lift.setTarget(6);
                    while (lift.isNotIdle()) {
                        Timer.delay(0.1);
                    }
                    drive.turnDegAbs(288, .5);
                    drive.moveDistance(10, 1);
                    grabber.outtake();

                    break;
                }
                break;
            case 5:
                // Out of the way left
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                drive.moveDistance(210, 1);
                drive.turnDegAbs(90, 0.5);
                drive.moveDistance(40, 1);
                break;
            case 6:
                // Out of the way right
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                drive.moveDistance(210, 1);
                drive.turnDegAbs(-90, 0.5);
                drive.moveDistance(40, 1);
                break;
            case 7:
                // Outtake into the hole
                // Shake a bit to drop the grabber
                drive.moveDistance(-5, .5);
                drive.moveDistance(5, .5);
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
        lift.start();
        lift.setSpeed(0.5);
        drive.zeroEnc();
        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        // Drive
        drive.arcadeDrive(JoystickUtils.processX(driveStick.getX()), JoystickUtils.processY(driveStick.getY()),
                JoystickUtils.processTwist(driveStick.getTwist()));

        // Drive Stick Debug
//        JoystickUtils.debug(driveStick.getX(), driveStick.getY(), driveStick.getTwist());

        // Dumper
        if (driveStick.getRawButton(Buttons.DUMPER)) {
            if (!dumper.isOpen()) {
                dumper.open();
                Timer.delay(.4);
            }
            dumper.set(DoubleSolenoid.Value.kForward);
        } else {
            if (dumper.hasBlock()) {
                dumper.close();
            } else {
                dumper.open();
            }
            dumper.set(DoubleSolenoid.Value.kReverse);
        }
        // Lift
        if (JoystickUtils.processY(liftStick.getY()) != 0) {
            lift.setManual(true);
            lift.setSpeedManual(JoystickUtils.processY(-liftStick.getY()));
            lift.resetTarget();
        } else {
            lift.setManual(false);
            if (liftStick.getRawButton(Buttons.LIFT[0])) {
                lift.setTarget(0);
            } else if (liftStick.getRawButton(Buttons.LIFT[1])) {
                lift.setTarget(2);
            } else if (liftStick.getRawButton(Buttons.LIFT[2])) {
                lift.setTarget(4);
            }
        }

        // Grabber
        if (liftStick.getRawButton(Buttons.GRABBER_OPEN)) {
            grabber.set(DoubleSolenoid.Value.kForward);
        } else if (liftStick.getRawButton(Buttons.GRABBER_CLOSE[0]) || liftStick.getRawButton(Buttons.GRABBER_CLOSE[1])
                || liftStick.getRawButton(Buttons.GRABBER_CLOSE[2])
                || liftStick.getRawButton(Buttons.GRABBER_CLOSE[3])) {
            grabber.set(DoubleSolenoid.Value.kReverse);
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

        // Gear Shift
        if (driveStick.getRawButton(Buttons.GEAR_HIGH)) {
            drive.gearSwitch(DoubleSolenoid.Value.kForward);
        } else if (driveStick.getRawButton(Buttons.GEAR_LOW)) {
            drive.gearSwitch(DoubleSolenoid.Value.kReverse);
        }
        drive.updateDashboard();
        dumper.updateDashboard();
        drive.debugEnc();
        Timer.delay(.01);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        drive.updateDashboard();
        dumper.updateDashboard();
        if (liftStick.getRawButton(1)) {
            dumper.set(DoubleSolenoid.Value.kForward);
        } else {
            dumper.set(DoubleSolenoid.Value.kReverse);
        }
        if (liftStick.getRawButton(2)) {
            dumper.open();
        } else {
            dumper.close();
        }
        SmartDashboard.putBoolean("Pressure", comp.getPressureSwitchValue());
        SmartDashboard.putBoolean("TooHigh", comp.getCompressorCurrentTooHighFault());
        SmartDashboard.putBoolean("NotConnected", comp.getCompressorNotConnectedFault());
        SmartDashboard.putBoolean("Shorted", comp.getCompressorShortedFault());
        Timer.delay(0.1);
    }

    public void disabledInit() {
        lift.stop();
    }
}

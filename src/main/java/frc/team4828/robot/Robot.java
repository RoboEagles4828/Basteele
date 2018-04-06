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
    private int mode;
    // Dumper
    private Dumper dumper;

    private Thread dashboardThread;

    // Auton
    private static final double[] MOVE = { 0.5, 0.7 };
    private static final double TURN = 0.3;
    private static final double LENGTH = 38;
    private static final double WIDTH = 34;
    private static final double SWITCH_INIT = 12;
    private static final double SWITCH_OUTER = 55.5 - WIDTH;
    private static final double SWITCH_INNER = 6;
    private static final double SCALE_OUTER = 42 - WIDTH;
    private static final double SCALE_INNER = 12;
    private static final double[] SCALE_OFFSET = { 6, 16 };

    public void robotInit() {
        // Joysticks
        driveStick = new Joystick(Ports.DRIVE_STICK);
        liftStick = new Joystick(Ports.LIFT_STICK);
        // Pneumatics
        // Drive
        drive = new DriveTrain(Ports.LEFT_GEARBOX, Ports.RIGHT_GEARBOX, Ports.SHIFTER);
        // Lift
        lift = new Lift(Ports.LIFT, Ports.LIFT_MIN, Ports.LIFT_MAX, Ports.SWITCHER);
        //liftr = new LiftRevamp(Ports.LIFT, Ports.LIFT_MAX, Ports.LIFT_MIN);
        // Grabber
        grabber = new Grabber(Ports.LEFT_GRABBER, Ports.RIGHT_GRABBER, Ports.GRABBER, Ports.FLIPPER);
        // Climber
        climber = new Climber(Ports.LEFT_CLIMBER, Ports.RIGHT_CLIMBER);
        // Auton
        switch1 = new DigitalInput(Ports.AUTON[0]);
        switch2 = new DigitalInput(Ports.AUTON[1]);
        switch3 = new DigitalInput(Ports.AUTON[2]);
        // Dumper
        dumper = new Dumper(Ports.DUMPER, Ports.SERVO, Ports.PROX);

        dashboardThread = new Thread() {
            public void run() {
                while (true) {
                    drive.updateDashboard();
                    dumper.updateDashboard();
                    lift.updateDashboard();
                    Timer.delay(0.1);
                }
            }
        };
        dashboardThread.start();

//        CameraServer.getInstance().startAutomaticCapture();
    }

    public void autonomousInit() {
        System.out.println(" --- Start Autonomous Init ---");
        doneAuton = false;
        data = DriverStation.getInstance().getGameSpecificMessage();
        mode = (switch1.get() ? 4 : 0) + (switch2.get() ? 2 : 0) + (switch3.get() ? 1 : 0);
        SmartDashboard.putNumber("Autonomous Mode", mode);

        drive.setGear(DoubleSolenoid.Value.kForward);
        drive.reset();
        drive.zeroEnc();
        Timer.delay(0.1);

        lift.start();

        dumper.close();
        dumper.set(DoubleSolenoid.Value.kReverse);

        System.out.println(" --- Start Autonomous ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            switch (mode) {
            case 0:
                // Switch from front
                switchAuton(0);
                break;
            case 1:
                // Switch from left
                switchAuton(-1);
                break;
            case 2:
                // Switch from right
                switchAuton(1);
                break;
            case 3:
                // Scale from left
                scaleAuton(-1, 0);
                break;
            case 4:
                // Scale from right
                scaleAuton(1, 0);
                break;
            case 5:
                // Scale+ from left
                scaleAuton(-1, 3);
                break;
            case 6:
                // Scale+ from right
                scaleAuton(1, 3);
                break;
            case 7:
                // Move Forward
                drive.moveDistance(240, MOVE[0]);
                break;
            case 10:
                // Out of the way left
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                drive.moveDistance(210, MOVE[1]);
                drive.turnDegAbs(90, TURN);
                drive.moveDistance(40, MOVE[1]);
                break;
            case 11:
                // Out of the way right
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                drive.moveDistance(210, MOVE[1]);
                drive.turnDegAbs(-90, TURN);
                drive.moveDistance(40, MOVE[1]);
                break;
            case 12:
                // Test
                drive.moveDistance(50, MOVE[0]);
                grabber.set(DoubleSolenoid.Value.kForward);
                grabber.intake();
                drive.moveDistance(10, MOVE[0]);
                grabber.set(DoubleSolenoid.Value.kReverse);
                Timer.delay(0.5);
                grabber.stop();
                drive.turnDegAbs(90, TURN);
                drive.moveDistance(10, 90, MOVE[0]);
                grabber.up();
                grabber.outtake();
                Timer.delay(1);
                grabber.stop();
                break;
            default:
                System.out.println("No Auton mode selected.");
                break;
            }
            doneAuton = true;
            System.out.println("Auton done");
        }
        Timer.delay(0.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");

        drive.setGear(DoubleSolenoid.Value.kForward);
        drive.zeroEnc();

        lift.start();

        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        // Drive
        drive.arcadeDrive(JoystickUtils.processX(driveStick.getX()), JoystickUtils.processY(driveStick.getY()),
                JoystickUtils.processTwist(driveStick.getTwist()));

        // Dumper
        if (driveStick.getRawButton(Buttons.DUMPER)) {
            if (!dumper.isOpen()) {
                dumper.open();
                Timer.delay(0.5);
            }
            dumper.set(DoubleSolenoid.Value.kForward);
        } else {
            dumper.set(DoubleSolenoid.Value.kReverse);
            if (dumper.hasBlock()) {
                if (dumper.isOpen()) {
                    Timer.delay(0.05);
                    dumper.close();
                }
            } else {
                dumper.open();
            }
        }

        // Lift
        if (JoystickUtils.processY(liftStick.getY()) != 0) {
            lift.setVelocity(JoystickUtils.processY(-liftStick.getY()));
        } else if (liftStick.getRawButton(Buttons.LIFT[0])) {
            lift.setVelocity(Lift.DEFAULT_SPEED);
        } else if (liftStick.getRawButton(Buttons.LIFT[1])) {
            lift.setVelocity(-Lift.DEFAULT_SPEED);
        } else {
            lift.setVelocity(0);
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
        if (liftStick.getRawButton(Buttons.FLIP)) {
            grabber.up();
        } else {
            grabber.down();
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
//        if (driveStick.getRawButton(Buttons.GEAR_HIGH)) {
//            drive.setGear(DoubleSolenoid.Value.kReverse);
//        } else if (driveStick.getRawButton(Buttons.GEAR_LOW)) {
//            drive.setGear(DoubleSolenoid.Value.kForward);
//        }

        Timer.delay(0.01);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");
        lift.start();
        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        lift.setVelocity(JoystickUtils.processY(-liftStick.getY()));
        Timer.delay(0.1);
    }

    public void disabledInit() {
        lift.stop();
    }

    private void switchAuton(int init) {
        int target = data.charAt(0) == 'L' ? -1 : 1;
        if (init == target) {
            drive.moveDistance(-140, MOVE[1]);
            drive.turnDegAbs(-target * 90, TURN);
        } else if (init == -target) {
            drive.moveDistance(-SWITCH_INIT, MOVE[1]);
            drive.turnDegAbs(init * 90, TURN);
            drive.moveDistance(144 + 2 * SWITCH_OUTER + WIDTH, MOVE[1]);
            drive.turnDegAbs(0, TURN);
            drive.moveDistance(SWITCH_INIT - 140, MOVE[1]);
            drive.turnDegAbs(-target * 90, TURN);
        } else if (target == -1) {
            drive.moveDistance(-SWITCH_INIT, MOVE[1]);
            drive.turnDegAbs(90, TURN);
            drive.moveDistance(72 + 2 * SWITCH_INNER + WIDTH, MOVE[1]);
            drive.turnDegAbs(0, TURN);
            drive.moveDistance(SWITCH_INIT + LENGTH - 130, MOVE[1]);
        } else {
            drive.moveDistance(LENGTH - 130, MOVE[1]);
        }
        drive.drive(-MOVE[0]);
        Timer.delay(1);
        drive.brake();
        dumper.open();
        Timer.delay(0.5);
        dumper.set(DoubleSolenoid.Value.kForward);
    }

    private void scaleAuton(int init, int type) {
        int switchTarget = data.charAt(0) == 'L' ? -1 : 1;
        int target = data.charAt(1) == 'L' ? -1 : 1;
        lift.setDirection(1);
        drive.moveDistance(222, MOVE[1]);
        drive.turnDegAbs(-init * 90, TURN);
        if (init == target) {
            drive.moveDistance(36 + SCALE_OUTER - SCALE_INNER, MOVE[1]);
        } else if (init == -target) {
            drive.moveDistance(144 + SCALE_OUTER + WIDTH + SCALE_INNER, MOVE[1]);
        }
        drive.turnDegAbs(0, TURN);
        drive.moveDistance(78 - SCALE_OFFSET[1] - LENGTH, MOVE[1]);
        while (lift.isBusy()) {
            Timer.delay(0.1);
        }
        drive.moveDistance(SCALE_OFFSET[1] - SCALE_OFFSET[0], MOVE[0]);
        grabber.set(DoubleSolenoid.Value.kForward);
        Timer.delay(0.5);
        drive.moveDistance(SCALE_OFFSET[0] - SCALE_OFFSET[1], MOVE[0]);
        lift.setDirection(-1);
        drive.turnDegAbs(180, TURN);
        drive.moveDistance(78 - SCALE_OFFSET[1] - LENGTH, MOVE[1]);
        while (lift.isBusy()) {
            Timer.delay(0.1);
        }
        grabber.set(DoubleSolenoid.Value.kForward);
        grabber.intake();
        drive.moveDistance(10, MOVE[1]);
        grabber.set(DoubleSolenoid.Value.kReverse);
        Timer.delay(0.5);
        grabber.stop();
        lift.setDirection(1);
        drive.moveDistance(-10, MOVE[1]);
        if (type > 1) {
            drive.turnDegAbs(-target * 90, TURN);
            if (switchTarget == target) {
                drive.moveDistance(1.5 + SCALE_INNER - SWITCH_INNER, MOVE[1]);
            } else if (type == 3) {
                drive.moveDistance(7.5 + SCALE_INNER + SWITCH_INNER + WIDTH, MOVE[1]);
            } else {
                return;
            }
            lift.setDirection(0);
            drive.turnDegAbs(180, TURN);
            drive.moveDistance(14, MOVE[0]);
            grabber.set(DoubleSolenoid.Value.kForward);
        } else if (type == 1) {
            drive.turnDegAbs(0, TURN);
            drive.moveDistance(78 - SCALE_OFFSET[1] - LENGTH, MOVE[1]);
            while (lift.isBusy()) {
                Timer.delay(0.1);
            }
            drive.moveDistance(SCALE_OFFSET[1] - SCALE_OFFSET[0], MOVE[0]);
            grabber.set(DoubleSolenoid.Value.kForward);
        } else {
            return;
        }
    }
}

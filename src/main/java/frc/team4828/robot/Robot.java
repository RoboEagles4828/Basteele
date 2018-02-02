package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    Gearbox leftGearbox;
    Gearbox rightGearbox;
    Joystick joystick;
    PneumaticSwitch dumper;

    DriveTrain drive;
    Grabber grabber;

    public void robotInit() {

        TalonSRX motor1 = new TalonSRX(Ports.LEFT_MOTORS[0]);
        TalonSRX motor2 = new TalonSRX(Ports.LEFT_MOTORS[1]);
        TalonSRX motor3 = new TalonSRX(Ports.RIGHT_MOTORS[0]);
        TalonSRX motor4 = new TalonSRX(Ports.RIGHT_MOTORS[1]);
        Compressor comp = new Compressor();
        DoubleSolenoid sol1 = new DoubleSolenoid(Ports.LEFT_SOLENOID[0], Ports.LEFT_SOLENOID[1]);
        DoubleSolenoid sol2 = new DoubleSolenoid(Ports.RIGHT_SOLENOID[0], Ports.RIGHT_SOLENOID[1]);
        PneumaticSwitch switcher1 = new PneumaticSwitch(comp, sol1);
        PneumaticSwitch switcher2 = new PneumaticSwitch(comp, sol2);

        leftGearbox = new Gearbox(motor1, motor2, switcher1);
        rightGearbox = new Gearbox(motor3, motor4, switcher2);

        joystick = new Joystick(Ports.JOYSTICK);

        DoubleSolenoid dumperSol = new DoubleSolenoid(Ports.DUMPER[0], Ports.DUMPER[1]);
        dumper = new PneumaticSwitch(comp, dumperSol);

        grabber = new Grabber(Ports.GRABBER[0], Ports.GRABBER[1]);

        drive = new DriveTrain(leftGearbox, rightGearbox);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");

        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {

        Timer.delay(.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");

        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        drive.jArcadeDrive(joystick.getX(), joystick.getY(), joystick.getTwist());
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

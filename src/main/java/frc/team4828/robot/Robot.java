package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    private TalonSRX motor1;
    private TalonSRX motor2;
    private TalonSRX motor3;
    private TalonSRX motor4;
    private Compressor comp;
    private DoubleSolenoid sol1;
    private DoubleSolenoid sol2;
    private PneumaticSwitch switcher1;
    private PneumaticSwitch switcher2;
    private Gearbox leftGearbox;
    private Gearbox rightGearbox;
    private DoubleSolenoid dumperSol;
    
    private Joystick joystick;

    private DriveTrain drive;
    private Grabber grabber;
    private PneumaticSwitch dumper;

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

        joystick = new Joystick(Ports.JOYSTICK);

        drive = new DriveTrain(leftGearbox, rightGearbox);
        grabber = new Grabber(Ports.GRABBER[0], Ports.GRABBER[1]);
        dumper = new PneumaticSwitch(comp, dumperSol);
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

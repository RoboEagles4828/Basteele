package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

    TalonSRX fl, fr, bl, br;
    Joystick j;

    public void robotInit() {
        fl = new TalonSRX(Ports.FRONT_LEFT);
        fr = new TalonSRX(Ports.FRONT_RIGHT);
        bl = new TalonSRX(Ports.BACK_LEFT);
        br = new TalonSRX(Ports.BACK_RIGHT);

        j = new Joystick(Ports.JOYSTICK);

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

        Timer.delay(.1);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");

        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        double speed1 = j.getThrottle();
        double speed2 = j.getThrottle();

        fl.set(ControlMode.PercentOutput, speed1);
        fr.set(ControlMode.PercentOutput, speed2);
        bl.set(ControlMode.PercentOutput, speed1);
        br.set(ControlMode.PercentOutput, speed2);

        Timer.delay(.1);
    }

}

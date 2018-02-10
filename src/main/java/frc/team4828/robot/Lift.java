package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Lift{

    DigitalInput armMin, armMax, liftMax, liftMin, hallEffect;
    Talon liftMotor, armMotor, leftGrabber, rightGrabber;
    public static final double DEFAULT_SPEED = 0.1;

    public Lift(int hePort, int liftMotorPort, int armMotorPort,  int armMaxPort, int armMinPort, int liftMaxPort, int liftMinPort, int leftGrabberPort, int rightGrabberPort){
        hallEffect = new DigitalInput(hePort);
        armMin = new DigitalInput(armMinPort);
        armMax = new DigitalInput(armMaxPort);
        liftMin = new DigitalInput(liftMinPort);
        liftMax = new DigitalInput(liftMaxPort);
        liftMotor = new Talon(liftMotorPort);
        armMotor = new Talon(armMotorPort);
        leftGrabber = new Talon(leftGrabberPort);
        rightGrabber = new Talon(rightGrabberPort);
    }
    public void armUp(){
        armMotor.set(DEFAULT_SPEED);
        while(!armMax.get()){
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }
    public void armUp(int speed){
        armMotor.set(speed);
        while(!armMax.get()){
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }
    public void armDown(){
        armMotor.set(-DEFAULT_SPEED);
        while(!armMin.get()){
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }
    public void armDown(int speed){
        armMotor.set(speed);
        while(!armMin.get()){
            Timer.delay(0.1);
        }
        armMotor.set(0);
    }
    public void liftUp(){
        liftMotor.set(DEFAULT_SPEED);
        while(!liftMax.get()){
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }
    public void liftUp(int speed){
        liftMotor.set(speed);
        while(!liftMax.get()){
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }
    public void liftDown(){
        liftMotor.set(-DEFAULT_SPEED);
        while(!liftMin.get()){
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }
    public void liftDown(int speed){
        liftMotor.set(speed);
        while(!liftMin.get()){
            Timer.delay(0.1);
        }
        liftMotor.set(0);
    }
    public void intake() {
        leftGrabber.set(1);
        rightGrabber.set(-1);
    }

    public void outtake() {
        leftGrabber.set(-1);
        rightGrabber.set(1);
    }

    public void stop() {
        leftGrabber.set(0);
        rightGrabber.set(0);
    }

    public void set(int speed) {
        leftGrabber.set(speed);
        rightGrabber.set(-speed);
    }
}

package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class LiftRevamp implements Runnable{
    private Victor liftMotor;
    private DigitalInput topLimit, botLimit;

    private static final double DEADZONE = .1;
    private static final double AUTO_SPD = .4;

    private int command = 0;
    private double speed = 0;
    private boolean moving = false, stopThread = false;

    public LiftRevamp(int motorPort, int topLimitPort, int botLimitPort) {
        liftMotor = new Victor(motorPort);
        topLimit = new DigitalInput(topLimitPort);
        botLimit = new DigitalInput(botLimitPort);
    }
    public void run() {
        while(!stopThread) {
            switch (command) {
                case 0:
                    move();
                    break;
                default:
                    auto();
            }
        }
        System.out.println("LiftR Stopped");
    }

    public void setSpeed(double value) {
        speed = value;
    }

    public void setCommand(int comm) {
        command = comm;
    }

    private void move() {
        double mspeed;
        if(command != 0) {
            mspeed = AUTO_SPD * command;
        } else {
            mspeed = speed;
        }
        if((mspeed < 0 && botLimit.get()) ||
           (mspeed > 0 && topLimit.get()) ||
           (Math.abs(mspeed) < DEADZONE)) {
            liftMotor.set(0);
            return;
        }
        liftMotor.set(mspeed);
    }

    private boolean check() {
        if(command == 1) {
            return topLimit.get();
        } else {
            return botLimit.get();
        }
    }

    private void auto() {
        moving = true;
        move();
        int oldComm = command;
        while(!check()) {
            System.out.println(command);
            if(command != oldComm) {
                break;
            }
            Timer.delay(.1);
        }
        command = 0;
        liftMotor.set(0);
        moving = false;
    }
    public void start() {
        if (stopThread) {
            stopThread = false;
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        stopThread = true;
    }

}

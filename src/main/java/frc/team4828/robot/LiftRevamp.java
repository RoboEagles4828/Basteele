package frc.team4828.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class LiftRevamp implements Runnable{
    private Victor liftMotor;
    private DigitalInput topLimit, botLimit;

    private static final double DEADZONE = .1;

    int command = 0;
    double speed = 0;
    boolean moving = false;

    public LiftRevamp(int motorPort, int topLimitPort, int botLimitPort) {
        liftMotor = new Victor(motorPort);
        topLimit = new DigitalInput(topLimitPort);
        botLimit = new DigitalInput(botLimitPort);
    }
    public void run() {
        switch (command) {
            case 0:
                move();
                break;
            default:
                auto();
        }
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
            mspeed = speed * command;
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
        while(!check()) {
            Timer.delay(.1);
        }
        liftMotor.set(0);
        moving = false;
    }
}

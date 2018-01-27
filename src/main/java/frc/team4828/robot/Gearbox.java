package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Gearbox {
    
    TalonSRX motor1;
    TalonSRX motor2;
    Pneumatic switcher;
    
    public Gearbox(TalonSRX motor1, TalonSRX motor2, Pneumatic switcher) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.switcher = switcher;
    }
    
    
    
}

package frc.team4828.robot;

public class DriveTrain {
    Gearbox left, right;
    public DriveTrain(Gearbox l, Gearbox r) {
        left = l;
        right = r;
    }
    public double[] normalize(double[] in) {
        double max = -10;
        for(int i = 0; i < in.length; i++) {
            if(in[i] > max) {
                max = in[i];
            }
        }
        max = Math.abs(max);
        for(int i = 0; i < in.length; i++) {
            in[i] /= max;
        }
        return in;
    }
    public void arcadeDrive(double x, double y, double twist) {
        double[] drive = { y - x - twist, y + x + twist };
        drive = normalize(drive);
        left.drive(drive[0]);
        right.drive(drive[1]);
    }

}

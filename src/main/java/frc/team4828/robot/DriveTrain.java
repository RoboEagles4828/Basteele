package frc.team4828.robot;

public class DriveTrain {
    private static final double TWIST_THRESH = .3;
    Gearbox left, right;
    public DriveTrain(Gearbox l, Gearbox r) {
        left = l;
        right = r;
    }
    public double[] normalize(double[] in) {
        double max = 0;
        for(int i = 0; i < in.length; i++) {
            double hold = Math.abs(in[i]);
            if(hold > max) {
                max = hold;
            }
        }
        //System.out.println(max);
        if(max > 1) {
            for (int i = 0; i < in.length; i++) {
                in[i] /= max;
            }
        }
        return in;
    }
    public void arcadeDrive(double x, double y, double twist) {
        if(Math.abs(twist) < TWIST_THRESH)
            twist = 0;
        else
            twist -= TWIST_THRESH;
        y = -y;
        double[] drive = { (y - x) + twist, (y + x)  -twist};
        drive = normalize(drive);
        left.drive(drive[0]);
        right.drive(drive[1]);
    }

    public void debug(double x, double y, double twist) {
        y = -y;
        double[] drive = { y - x, y + x };
        drive = normalize(drive);
        //System.out.println(x + "  " + y + "  " + twist);
        System.out.println(drive[0] + "  " + drive[1]);
    }

}

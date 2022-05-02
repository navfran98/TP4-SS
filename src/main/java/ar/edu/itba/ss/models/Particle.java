package ar.edu.itba.ss.models;

public class Particle {

    // Varible
    private final double mass;
    private final double x;
    private final double y;
    private final double vx;
    private final double vy;
    private final double charge;

    // Constructor
    public Particle(double mass, double x, double y, double vx, double vy, double charge) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.charge = charge;
    }

    // Getters
    public double getMass() {
        return mass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getCharge() {
        return charge;
    }
}

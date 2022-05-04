package ar.edu.itba.ss.models;

public class Particle {

    // Varible
    private final Double mass;
    private final Double x;
    private final Double y;
    private final Double vx;
    private final Double vy;
    private Double charge;

    // Constructor
    public Particle(Double mass, Double x, Double y, Double vx, Double vy) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public Particle(Double mass, Double x, Double y, Double vx, Double vy, Double charge) {
        this.mass = mass;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.charge = charge;
    }

    // Getters
    public Double getMass() {
        return mass;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getVx() {
        return vx;
    }

    public Double getVy() {
        return vy;
    }

    public Double getCharge() {
        return charge;
    }
}

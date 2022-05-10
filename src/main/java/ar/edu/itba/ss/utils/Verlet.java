package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.*;
import ar.edu.itba.ss.models.*;

public class Verlet implements AlgorithmInterface{

    public Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt, boolean electric) {
        Force currentForce = getForce(electric, currentParticle);
        if(prevParticle.getX() == null) {
            prevParticle = Euler.getValuesForFirstPreviousParticle(currentParticle, currentForce, dt);
        }
        double newX = 2 * currentParticle.getX() - prevParticle.getX() + ((dt * dt)/currentParticle.getMass()) * currentForce.getX();
        double newY = 2 * currentParticle.getY() - prevParticle.getY() + ((dt * dt)/currentParticle.getMass()) * currentForce.getY();

        double newVx = (newX - prevParticle.getX())/(2*dt);
        double newVy = (newY - prevParticle.getY())/(2*dt);

        return new Particle(currentParticle.getMass(), newX, newY, newVx, newVy, currentParticle.getCharge());
    }

    private Force getForce(boolean electric, Particle p){
        if(electric)
            return ElectricUniverse.calculateForce(p);
        else
            return OsciladorAmortiguado.calculateForce(p);
    }
}
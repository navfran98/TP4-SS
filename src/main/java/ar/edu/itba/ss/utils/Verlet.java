package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.App;
import ar.edu.itba.ss.models.*;

public class Verlet {

    public static Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt) {
        double currentForce = App.calculateForce(currentParticle);
        if(prevParticle.getX() == null) {
            prevParticle = Euler.getValuesForFirstPreviousParticle(currentParticle, currentForce, dt);
        }
        double newX = 2 * currentParticle.getX() - prevParticle.getX() + ((dt * dt)/currentParticle.getMass()) * currentForce;
        // double newY = 2 * currentParticle.getY() - prevParticle.getY() + ((dt * dt)/currentParticle.getMass()) * currentForce;

        double newVx = (newX - prevParticle.getX())/(2*dt);
        // double newVy = (newY - prevParticle.getY())/(2*dt);

        return new Particle(currentParticle.getMass(), newX, 0.0, newVx, 0.0);
    }


    public static Pair<Double, Double> getNextPos1D(Double x, Double v, Double m, Double t, Double dt, Double f) {
        // TODO: el siguiente codigo esta comentado porque no me cierra matematicamente el calculo de getPreviousX_1D()
//        double prevX = Euler.getPreviousX_1D(x, v, f, m, t);
//        double newX = 2 * x - prevX + (dt*dt * f / m);
//        double newV = (newX - prevX)/(2*dt);
        double newX = x + (dt*v) + (dt*dt*f/(2*m));
        double newV = v + (dt*f/m);
        return new Pair<>(newX, newV);
    }
}
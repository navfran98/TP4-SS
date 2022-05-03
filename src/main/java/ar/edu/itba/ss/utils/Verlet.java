package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.*;

public class Verlet {

    public Particle getNextPos(Particle currentParticle, Particle prevParticle, double t) {
        // 1. si tengo la primer particula, uso euler para obtener la posicion para r(t-delta t)
        // if(prevParticle is null)
        // prevParticle = Euler.getValuesForFirstPreviousParticle(currentPart);

        // 2. Calculo el nuevo x e y, usando rx = 2*currentPart.getx() - prevPart.getx() + ((delta t ^2)/masa) * fuerza de currentPart
        // double newX = 2 * currentParticle.getX() - prevParticle.getX() + ((t * t)/currentParticle.getMass()) * currentParticleForce.getX();
        // double newY = 2 * currentParticle.getY() - prevParticle.getY() + ((t * t)/currentParticle.getMass()) * currentParticleForce.getY();

        // 3. Calculo las nuevas vx e vy usando vx = (newvalues.getx() -
        // prevPart.getx())/ (2* delta t)
        // double newVx = (newX - prevParticle.getX())/(2*t);
        // double newVy = (newY - prevParticle.getY())/(2*t);

        // 4. Armo una particula nueva con dichos valores y la retorno
        // return new Particle(currentParticle.getMass(), newX, newY, newVx, newVy,
        // currentParticle.getCharge());
        return currentParticle;
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
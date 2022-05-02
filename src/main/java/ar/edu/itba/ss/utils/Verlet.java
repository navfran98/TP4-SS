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
}
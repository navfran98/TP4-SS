package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.*;

public class Beeman {

    public Particle getNextValues(Particle currentPart, Particle prevPart, double t) {
        // Aca tambien vamos a tener que usar Euler para el caso de la primer particula
        // Es similar al Verlet pero se calculan los valores de la velocidad distinto, y se corrigen tmb
        // if(prevParticle is null)
        // prevParticle = Euler.getValuesForFirstPreviousParticle(currentPart)

        // 1. Se calcula las aceleracion para currentPart y prevPart porq se usan ambas en la ec de newV
        // double currentAceleration = currentParticleForce / currentPart.getMass();
        // double prevAceleration = prevParticleForce / prevPart.getMass();

        // 2. Se calculan los rx e ry usando ->
        // double newX = currentPart.getX() + currentPart.getVx() * t + ((2/3) * currentAceleration * (t * t)) - ((1/6) * prevAceleration * (t * t));
        // double newY = currentPart.getY() + currentPart.getVy() * t + ((2/3) * currentAceleration * (t * t)) - ((1/6) * prevAceleration * (t * t));

        // 3. Se calculan los valores predecidos para vx y vy usando ->
        // double prevVx = currentPart.getVx() + ((3/2) * currentAceleration * t) - ((1/2) * prevAceleration * t);
        // double prevVy = currentPart.getVy() + ((3/2) * currentAceleration * t) - ((1/2) * prevAceleration * t);

        // 4. Con estos valores se crea una nueva particula y se obtienen las nuevas aceleraciones porq vamos a necesitar a(t + delta t)
        // Particle auxParticle = new Particle(currentPart.getMass(), newX, newY, prevVx, prevVy, currentPart.getCharge());
        // double auxAceleration = auxParticleForce / auxParticle.getMass();

        // 5. Se calculan los valores corregidos para prevvx y prevvy usando ->
        // double newVx = prevVx + ((1/3) * auxAceleration * t) + ((5/6) * currentAceleration * t) - ((1/6) * prevAceleration * t);
        // double newVy = prevVy + ((1/3) * auxAceleration * t) + ((5/6) * currentAceleration * t) - ((1/6) * prevAceleration * t);

        // 6. Se crea una nueva particula con estas nuevas velocidades y se retorna.
        // return new Particle(currentPart.getMass(), newX, newY, newVx, newVy,
        // currentPart.getCharge());
        return currentPart;
    }
}

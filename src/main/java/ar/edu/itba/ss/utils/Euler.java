package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.*;

public class Euler {
    // Aca vamos a tener que tener una funcion sola, que se usa desde Verlet y
    // Beeman para el caso inicial

    public static Particle getValuesForFirstPreviousParticle(Particle p, Force force, double dt) {
        // Usando las ecuaciones de Euler per en lugar de usar (t + delta t), seria para (t - delta t)
        // 1. Calcular vx y vy usando -> newvx = vx - (delta t * fuerza)/masa
        double newVx = p.getVx() - (dt * force.getX())/p.getMass();
        double newVy = p.getVy() - (dt * force.getY())/p.getMass();
    
        // 2. Calcular rx y rx usando las newvx y newvy -> newrx = rx - delta t * newvx - ((delta t)^2 * fuerza)/ (2*masa)
        double newX = p.getX() - dt * newVx - ((dt * dt) * force.getX())/(2 * p.getMass());
        double newY = p.getY() - dt * newVy - ((dt * dt) * force.getY())/(2 * p.getMass());
        return new Particle(p.getMass(), newX, newY, newVx, newVy, p.getCharge());
    }
}

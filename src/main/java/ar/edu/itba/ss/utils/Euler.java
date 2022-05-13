package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.*;

public class Euler {

    public static Particle getValuesForFirstPreviousParticle(Particle p, Force force, double dt) {
        // Usando las ecuaciones de Euler per en lugar de usar (t + delta t), seria para (t - delta t)
        // 1. Calcular vx y vy usando -> newvx = vx - (delta t * fuerza)/masa
        double newVx = p.getVx() + (dt/p.getMass())* force.getX();
        double newVy = p.getVy() + (dt/p.getMass())* force.getY();
    
        // 2. Calcular rx y rx usando las newvx y newvy -> newrx = rx - delta t * newvx - ((delta t)^2 * fuerza)/ (2*masa)
        double newX = p.getX() + dt * newVx + ((dt*dt) * force.getX())/(2*p.getMass());
        double newY = p.getY() + dt * newVy + ((dt*dt) * force.getY())/(2*p.getMass());
        return new Particle(p.getMass(), newX, newY, newVx, newVy, p.getCharge());
    }
}

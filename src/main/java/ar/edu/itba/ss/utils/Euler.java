package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.*;

public class Euler {
    // Aca vamos a tener que tener una funcion sola, que se usa desde Verlet y
    // Beeman para el caso inicial

    public Particle getValuesForFirstPreviousParticle(Particle p, double t) {
        // Usando las ecuaciones de Euler per en lugar de usar (t + delta t), seria para (t - delta t)
        // 1. Calcular vx y vy usando -> newvx = vx - (delta t * fuerza)/masa
        // double newVx = p.getVx() - (t * particleForce)/p.getMass();
        // double newVy = p.getVy() - (t * particleForce)/p.getMass();

        // 2. Calcular rx y rx usando las newvx y newvy -> newrx = rx - delta t * newvx - ((delta t)^2 * fuerza)/ (2*masa)
        // double newX = p.getX() - t * newVx - ((t * t) * particleForce)/(2 * p.getMass());
        // double newX = p.getX() - t * newVx - ((t * t) * particleForce)/(2 * p.getMass());
        // return new Particle(p.getMass(), newX, newY, newVx, newVy, p.getCharge());
        return p;
    }


    // TODO: no me cierra matematicamente este calculo
    public static Double getPreviousX_1D(Double x, Double v, Double f, Double m, double t) {
        double newVx = v - (t * f)/m;
        return x - (t * newVx) - ((t * t) * f)/(2 * m);
    }
}

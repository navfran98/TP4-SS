package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

public interface AlgorithmInterface {
    Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt);
}

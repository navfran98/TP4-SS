package ar.edu.itba.ss.models;

import ar.edu.itba.ss.parsers.OutputParser;

import java.util.ArrayList;
import java.util.List;

public class Universe {

    private static final Double k = 1e10;
    private static final Double Q = 1e-19;
    private static final Double M = 1e-27;
    private static final Double D = 1e-8;
    private static final Double L = 15 * D;
    private static final Double v0 = 5e3;

    private final List<Particle> particles;

    public Universe() {
        particles = new ArrayList<>();
        populate();
    }

    private void populate() {
        Particle firstParticle = new Particle(M, 0.0, L/2, v0, 0.0, Q);
        particles.add(firstParticle);

        boolean on = true;
        for (int i = 1; i <= 16; i++) {
            for (int j = 0; j < 16; j++) {
                double charge;
                if(on) {
                    charge = Q;
                } else {
                    charge = -Q;
                }
                on = !on;
                Particle p = new Particle(M, i*D, j*D, 0.0, 0.0, charge);
                particles.add(p);
            }
            on = !on;
        }

        OutputParser.writeUniverse(particles);
    }

}

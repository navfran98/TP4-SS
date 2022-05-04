package ar.edu.itba.ss;

import ar.edu.itba.ss.models.*;
import ar.edu.itba.ss.parsers.*;
import ar.edu.itba.ss.utils.*;

public class OsciladorAmortiguado {

    // simulate(algorithm, currentPart, prevPart) aca va a ir evolucionando las posiciones, 
    // llamando a una funcion de getNextPos y te devuelve una nueva particula
    private static final Double m = 70.0;
    private static final Double k = 10000.0;
    private static final Double gamma = 100.0;
    private static final Double tf = 5.0;

    private static final Double A = 2.0;

    private static final Double r0 = 1.0;
    private static final Double v0 = -A * gamma / (2*m);

    private static final Double dt = 0.01;

    // Cada cuanto generamos output.
    private static final Double tOutput = 3*dt;

    public static void simulate(AlgorithmInterface algorithm, String filename) {
        
        Particle p = new Particle(m, r0, 0.0, v0, 0.0);
        Particle prevp = new Particle(m, null, null, null , null);
        Particle aux = null;
        OutputParser.createCleanPythonFile(filename);
        
        double auxt = 0;
        for (double t = 0 ; t <= tf; t += dt, auxt += dt) {
            // queremos calcular la siguiente posicion de p
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt);
            prevp = aux;
            //imprimir cada tOutput
            if(auxt == tOutput){
                OutputParser.writePythonCSV(calculateForce(p), p.getX(), p.getVx(), t, filename);
                auxt = 0;
            }
        }

        OutputParser.createCleanUniverseFile();
        Universe u = new Universe();
    }

    public static double calculateForce(Particle p){
        return ((- k * p.getX()) - (gamma * p.getVx()));
    }

}

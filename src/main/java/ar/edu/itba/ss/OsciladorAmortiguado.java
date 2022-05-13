package ar.edu.itba.ss;

import ar.edu.itba.ss.models.*;
import ar.edu.itba.ss.parsers.*;
import ar.edu.itba.ss.utils.*;

public class OsciladorAmortiguado {

    private static final double m = 70.0;
    private static final double k = 10000.0;
    private static final double gamma = 100.0;
    private static final double tf = 5.0;

    private static final double r0 = 1.0;
    private static final double v0 =  - gamma / (2*m);

    public static void simulate(AlgorithmInterface algorithm, String filename, double dt) {
        
        Particle p = new Particle(m, r0, 0.0, v0, 0.0, null);
        Particle prevp = new Particle(m, null, null, null , null, null);
        Particle aux = null;
        OutputParser.createCleanPythonFile(filename);
        OutputParser.writePythonCSV(calculateForce(p), p.getX(), p.getVx(), 0, filename);
        double tOutput = 4 * dt;
        double auxt = 0;
        for (double t = 0 ; t <= tf; t += dt, auxt += dt) {
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt, false);
            prevp = aux;
            // OutputParser.writePythonCSV(calculateForce(p), p.getX(), p.getVx(), t, filename);
            if(auxt == tOutput){
                OutputParser.writePythonCSV(calculateForce(p), p.getX(), p.getVx(), t, filename);
                auxt = 0;
            }
        }
    }

    public static Force calculateForce(Particle p){
        double forcex = ((- k) * p.getX() - gamma * p.getVx());
        return new Force(forcex, 0.0);
    }

    public static void calculateAnalyticSolution(String filename, double dt){
        Particle particle = new Particle(m, r0, 0.0, v0, 0.0);
        OutputParser.createCleanPythonFile(filename);
        OutputParser.writePythonAnalyticCSV(0, particle.getX(), filename);
        double tOutput = 4 * dt;
        double auxt = 0;
        for (double t = 0 ; t <= tf; t += dt, auxt += dt) {
            double r = Math.exp(-(gamma/(2 * m)) * t) * (Math.cos(Math.pow((k/m) - ((gamma * gamma)/(4 * m * m)), 0.5)*t));
            // particle = new Particle(m, r, 0.0, 0.0, 0.0);
            if(auxt == tOutput){
                OutputParser.writePythonAnalyticCSV(t, r, filename);
                auxt = 0;
            }
        }
    }
}

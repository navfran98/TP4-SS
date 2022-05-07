package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.parsers.OutputParser;
import ar.edu.itba.ss.utils.AlgorithmInterface;

import java.util.ArrayList;
import java.util.List;

public class ElectricUniverse {

    private static final Double k = 1e10;
    private static final Double Q = 1e-19;
    private static final Double M = 1e-27;
    private static final Double D = 1e-8;
    private static final Double L = 15 * D; //TODO: aca pusiste 15 pero no es 16?
    private static final Double Dcut = 0.01*D;
    private static final Double min_v0 = 5e3;
    private static final Double max_v0 = 5e4;

    private static final Double dt = 0.00001;
    private static final Double tf = 5.0;
    private static final Double tOutput = 5*dt;

    private static final List<Particle> particles = populate();

    private static List<Particle> populate() {
        Particle firstParticle = new Particle(M, 0.0, L/2, min_v0, 0.0, Q);
        List<Particle> particles = new ArrayList<>();
        // particles.add(firstParticle);
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
        return particles;
    }

    //siento q el simulate deberia recibir el tiempo final y el dt desde el app.java
    public void simulate(AlgorithmInterface algorithm, String filename){
        Particle p = new Particle(M, 0.0, L/2, min_v0, 0.0, Q);
        Particle prevp = new Particle(M, null, null, null , null, null);
        Particle aux = null;
        boolean shouldEnd = false;
        OutputParser.createCleanPythonFile(filename);
        OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), 0, filename);
        double auxt = 0;
        for (double t = 0 ; t <= tf && !shouldEnd; t += dt, auxt += dt) {
            // queremos calcular la siguiente posicion de p
            System.out.println("x: " + p.getX());
            System.out.println("y: " + p.getY());
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt, true);
            prevp = aux;
            if(auxt == tOutput){
                OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), t, filename);
                auxt = 0;
            }

            if(meetEndCondition(p)){
                shouldEnd = true;
            }
        }
    }

    private static boolean meetEndCondition(Particle p){
        //check si se fue por un borde
        for(Particle p2 : particles){
            if(p.getDistance(p2) < Dcut)
                return true;
        }
        if(p.getX() < 0 || p.getX() > L+D || p.getY() < 0 || p.getY() > L){
            System.out.println("Me voy por borde");
            return true;
        }
        return false;
    }

    public static Force calculateForce(Particle p) {
        Force dump = new Force(0.0, 0.0);
        Force aux = null;
        for(Particle part : particles) {
            aux = getVersor(p, part);
            double dist = p.getDistance(part);
            dump.sum((part.getCharge() / (dist * dist)) * aux.getX(), (part.getCharge() / (dist * dist)) * aux.getY());
            // dump.setX(dump.getX()+((part.getCharge() / (dist * dist)) * aux.getX()));
            // dump.setY(dump.getY()+((part.getCharge() / (dist * dist)) * aux.getY()));
        }
        dump.setX(k * p.getCharge() * dump.getX());
        dump.setY(k * p.getCharge() * dump.getY());
        return dump;
    }

    public static Force getVersor(Particle p1, Particle p2){
        double deltaX = p1.getX() - p2.getX();
        double deltaY = p1.getY() - p2.getY();
        double norm = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        return new Force(deltaX/norm, deltaY/norm);
    } 

    public double getPotencialEnergy(Particle p){
        double sum = 0;
        for(Particle part : particles) {
            sum += part.getCharge() / (p.getDistance(part));
        }
        return k * p.getCharge() * sum;
    }

}

package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Force;
import ar.edu.itba.ss.models.Pair;
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
    private static final Double L = 15 * D;
    private static final Double Dcut = 0.01*D;
    private static final Double min_v0 = 5e3;
    private static final Double max_v0 = 5e4;

    private static final Double tf = 5.0;

    private static Particle first_particle = new Particle(M, 0.0, randomY(), max_v0, 0.0, Q);
    private static final List<Particle> particles = populate();

    
    private static List<Particle> populate() {
        // Particle firstParticle = new Particle(M, 0.0, L/2, min_v0, 0.0, Q);
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

        OutputParser.writeUniverse("OutputTP4.xyz",first_particle, particles);
        return particles;
    }

    public void simulate(AlgorithmInterface algorithm, String filename, double dt, double tOutput, double posY){
        Particle p = new Particle(M, 0.0, posY, max_v0, 0.0, Q);;
        Particle prevp = new Particle(M, null, null, null , null, null);
        Particle aux = null;
        double total_energy = getKineticEnergy(p) + getPotencialEnergy(p);
        OutputParser.createCleanPythonFile(filename);
        OutputParser.OutputEj1(filename, 0, total_energy);
        // OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), 0, filename);
        boolean shouldEnd = false;
        int i = 0;
        double auxt = 0;
        for (double t = 0 ; t <= tf && !shouldEnd; t += dt, auxt += dt, i++) {
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt, true);
            prevp = aux;
            // total_energy = getKineticEnergy(p) + getPotencialEnergy(p);
            // OutputParser.OutputEj1(filename, t, total_energy);
            // OutputParser.writeUniverse(p, particles);
            if(auxt == tOutput){
                total_energy = getKineticEnergy(p) + getPotencialEnergy(p);
                OutputParser.OutputEj1(filename, t, total_energy);
                // OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), t, filename);
                auxt = 0;
            }

            if(meetEndCondition(p)){
                shouldEnd = true;
                System.out.println("la simulacion termino en la iteracion N°" + i);
            }
        }
    }

    public Pair<Double, Integer> simulateEj2(AlgorithmInterface algorithm, double dt, double posY, double vx, String fn, boolean parse, String xyz){
        Particle p = new Particle(M, 0.0, posY, vx, 0.0, Q);;
        Particle prevp = new Particle(M, null, null, null , null, null);
        Particle aux = null;
        boolean shouldEnd = false;
        double trajectory = 0;
        Integer end = 0;
        int auxt = 0;
        double tOutput = 2 * dt;
        if(parse) {
            // OutputParser.createCleanPythonTrajectoryFIle(fn);
            // OutputParser.parseTrajectory(fn, p.getX(), p.getY());
            // OutputParser.createCleanUniverseFile(xyz);
            // OutputParser.writeUniverse(xyz, p, particles);
        }
        for (int t = 0 ; t <= tf/dt && !shouldEnd; t++, auxt++) {
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt, true);
            prevp = aux;          
            trajectory += aux.getDistance(p);
            // if(parse)
            //     OutputParser.writeUniverse(xyz, p, particles);
            // if(auxt * dt == tOutput){
            //     if(parse)
            //         OutputParser.parseTrajectory(fn, p.getX(), p.getY());
            //     auxt = 0;
            // }

            end = endLocation(p);
            if(end >= 0){
                shouldEnd = true;
            }
        }
        return new Pair<Double,Integer>(trajectory, end);
    }

    private static int endLocation(Particle p){
        //check si se fue por un borde
        for(Particle p2 : particles){
            if(p.getDistance(p2) < Dcut) {
                System.out.println("Absorbido");
                return 0;
            }
        }
        if(p.getX() < 0){
            System.out.println("Izq");
            return 1; //pared izq
        }
        else if(p.getX() > L+D){
            System.out.println("Der");
            return 2; //pared derecha
        }
        else if (p.getY() < 0){
            System.out.println("Abajo");
            return 3;   //pared abajo
        }else if (p.getY() > L){
            System.out.println("Arriba");
            return 4;   //pared arriba
        }else
            return -1;
    }

    private static boolean meetEndCondition(Particle p){
        //check si se fue por un borde
        for(Particle p2 : particles){
            if(p.getDistance(p2) < Dcut) {
                System.out.println("Choque contra una particula");
                return true;
            }
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

    public double getKineticEnergy(Particle p){
        double v = Math.sqrt(p.getVx() * p.getVx() + p.getVy() * p.getVy());
        return 0.5 * p.getMass() * v * v;
    }
    
    public static Double randomY() {
        double min_y = 15/2.0 * D - D;
        double max_y = 15/2.0 * D + D;
        double y = Math.random() * (max_y - min_y) + min_y;
        return y;
    }

    public static Double randomVx() {
        double vx = Math.random() * (max_v0 - min_v0) + min_v0;
        return vx;
    }
}


// En el ej2.1 -> vamos a tener q imprimir para 5 dt distintos 5 archivos para cada uno de las 
// 5 posiciones fijas (pos1,...,pos5) usando max_v0. Donde para cada t guardamos la energia total
// Promediamos la energia total para un dt y los 5 archivos de las posiciones.

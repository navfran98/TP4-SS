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
    private static final Double L = 15 * D;
    private static final Double Dcut = 0.01*D;
    private static final Double min_v0 = 5e3;
    private static final Double max_v0 = 5e4;

    private static final Double dt = 1e-16;
    private static final Double tf = 5.0;
    private static final Double tOutput = 20*dt;

    // private static final Double pos1 = 15/2 * D + D/2;
    // private static final Double pos2 = L/2;
    // private static final Double pos3 = 15/2 * D - D/2;
    // private static final Double pos4 = (pos1 + pos2)/2;
    // private static final Double pos5 = (pos3 + pos2)/2;

    private static Particle first_particle = new Particle(M, 0.0, randomY(), randomVx(), 0.0, Q);
    private static final List<Particle> particles = populate();

    // 2.1) Para el mayor valor de V0 y 5 posiciones iniciales fijas en el rango especificado, simular el sistema usando varios dt con alguno de los integradores implementados en 1). Promediar para las 5
    // partículas, la evolución de la energía total del sistema (ET = cinética + potencial) y compararla para
    //los distintos dt elegidos. Para esto considerar el promedio sobre todos los dt de las diferencias relativas: |ET(t=0)-ET(ti>0)| / ET(t=0). De ser necesario usar escala logarítmica para mostrar los detalles
    // de la diferenci a ¿Cuál es el mejor dt para este sistema? ¿Cuál fue el criterio utilizado?

    
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

        OutputParser.writeUniverse(first_particle, particles);
        return particles;
    }

    //siento q el simulate deberia recibir el tiempo final y el dt desde el app.java
    public void simulate(AlgorithmInterface algorithm, String filename){
        Particle p = first_particle;
        Particle prevp = new Particle(M, null, null, null , null, null);
        Particle aux = null;
        OutputParser.createCleanPythonFile(filename);
        // String ej1String = "Output_Pos1_dt1.csv";
        // OutputParser.createCleanPythonFile(ej1String);
        // OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), 0, filename);
        boolean shouldEnd = false;
        double auxt = 0, i = 0;
        for (double t = 0 ; t <= tf && !shouldEnd; t += dt, auxt += dt, i++) {
            // queremos calcular la siguiente posicion de p
            aux = p;
            p = algorithm.getNextValues(p, prevp, dt, true);
            prevp = aux;
            OutputParser.writeUniverse(p, particles);
            if(auxt == tOutput){
                double total_energy = getKineticEnergy(p) + getPotencialEnergy(p);
                // OutputParser.OutputEj1(ej1String, t, total_energy);
                // OutputParser.writeElectricPythonCSV(calculateForce(p), p.getX(), p.getY(), p.getVx(), p.getVy(), t, filename);
                auxt = 0;
            }

            if(meetEndCondition(p)){
                shouldEnd = true;
                System.out.println("la simulacion termino en la iteracion N°" + i);
            }
        }
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
    
    private static Double randomY() {
        double min_y = 15/2.0 * D - D;
        double max_y = 15/2.0 * D + D;
        double y = Math.random() * (max_y - min_y) + min_y;
        return y;
    }

    private static Double randomVx() {
        double vx = Math.random() * (max_v0 - min_v0) + min_v0;
        return vx;
    }
}


// En el ej2.1 -> vamos a tener q imprimir para 5 dt distintos 5 archivos para cada uno de las 
// 5 posiciones fijas (pos1,...,pos5) usando max_v0. Donde para cada t guardamos la energia total
// Promediamos la energia total para un dt y los 5 archivos de las posiciones.

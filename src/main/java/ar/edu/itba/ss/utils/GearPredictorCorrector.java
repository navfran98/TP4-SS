package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;
import java.util.ArrayList;
import java.util.List;

public class GearPredictorCorrector implements AlgorithmInterface{

    private static final double[] alphas = { 3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60 };
    private static final int ORDER = 5;
    private static final Double k = 10000.0;
    private static final Double gamma = 100.0;

    public Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt, boolean electric) {

        List<Double> predictions = predict(currentParticle, dt);

        Particle nextP = new Particle(currentParticle.getMass(), predictions.get(0), 0.0, predictions.get(1), 0.0, currentParticle.getCharge());

        Double deltaR2 = evaluate(predictions, nextP, dt);

        List<Double> corrections = correct(predictions, deltaR2, dt);

        return new Particle(currentParticle.getMass(), corrections.get(0), 0.0, corrections.get(1), 0.0, currentParticle.getCharge());
    }

    private static List<Double> predict(Particle p, Double dt) {
        List<Double> response = new ArrayList<>();
        Double dump = 0.0;

        for (int i = 0; i < ORDER + 1; i++) {
            for (int j = i; j < ORDER + 1; j++) {
                dump += polPart(i, j, p, dt);
            }
            response.add(dump);
            dump = 0.0;
        }
        return response;
    }

    private static Double polPart(int i, int j, Particle p, Double dt) {
        Double response = 0.0;
        if( i == j ) { // termino inicial
            response = polPartDer(p, i); // en este caso es indistinto que le pasamos si i o j ...
        } else { // para el resto de los terminos salen de una sumatoria ==> j >= 1
            response = polPartDer(p, j); // aca si o si tiene que ser j ...
            response *= Math.pow(dt, j-i)/factorial(j-i);
        }
        return response;
    }

    private static Double polPartDer(Particle p, int i) {
        double r0 = p.getX();
        double r1 = p.getVx();
        double r2 = (- k * p.getX() - gamma * p.getVx())/p.getMass();
        double r3 = -k/p.getMass() * r1 - gamma/p.getMass() * r2;
        double r4 = -k/p.getMass() * r2 - gamma/p.getMass() * r3;
        double r5 = -k/p.getMass() * r3 - gamma/p.getMass() * r4;
        switch (i) { // segun que iteracion estemos
            case 0:
                return r0;
                // return p.getX(); // r0
            case 1:
                return r1;
                // return p.getVx(); // r1
            case 2:
                return r2;
                // return (- k * p.getX() - gamma * p.getVx())/p.getMass(); // r2 = fuerza/m
            case 3:
                return r3;
                // return (double)(-k/p.getMass()) * p.getVx(); // r3 = -k/m * r1
            case 4:
                return r4;
                // return (double)(-k/p.getMass()) * ((- k * p.getX() - gamma * p.getVx())/p.getMass()); // r4 = -k/m * r2
            case 5:
                return r5;
                // return (double)(-k/p.getMass()) * ((double)(-k/p.getMass())*p.getVx()); // r5 = -k/m * r3
        }
        return null;
    }

    private static Double factorial(int n) {
        if(n == 1 || n == 0)
            return 1.0;
        return n * factorial(n-1);
    }

    private static Double evaluate(List<Double> pred, Particle p, Double dt) {
        Double aDT = (-k * p.getX() - gamma * p.getVx())/p.getMass();
        Double apDT = pred.get(2);
        Double dt2 = dt * dt;

        return ((aDT-apDT)*dt2)/2;
    }

    private static List<Double> correct(List<Double> pred, Double deltaR2, Double dt) {
        List<Double> response = new ArrayList<>();

        response.add(pred.get(0) + (alphas[0] * deltaR2)); // posicion
        response.add(pred.get(1) + (alphas[1] * deltaR2/dt)); // velocidad

        return response;
    }
}
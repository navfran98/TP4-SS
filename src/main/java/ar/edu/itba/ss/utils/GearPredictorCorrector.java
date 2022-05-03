package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

public class GearPredictorCorrector {

    private final double[] alphas = { 3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60 };
    private final double[] factorials = {1.0, 1.0, 2.0, 6.0, 24.0, 120.0};
    private final int order = 5;

    // getNextPos(algorithm, currentPart, PrevPart) esta funcion va a tener q llamar a un predict, evaluate, correct
    public void getValuesForFirstPreviousParticle(Particle currentParticle, Particle prevParticle, double t) {

    }

    // usando las posiciones derivadas, se predicen nuevas posiciones usando los factoriales
    private void predict() {

    }

    // evaluas la fuerza en funcion de la aceleracion
    private void evaluate() {

    }

    // va a usar los alphas para ir corrigiendo todos los valores
    private void correct() {

    }

}
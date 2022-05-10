package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.*;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.parsers.*;

public class App {

    private static final String verletFile = "OutputVerlet.csv";
    private static final String beemanFile = "OutputBeeman.csv";
    private static final String gpcFile = "OutputGPC.csv";
    private static final String analyticFile = "OutputAnalyticSolution.csv";
    private static final String electricFile = "OutputElectric.csv";

    public static void main(String[] args) {
        // OutputParser.createCleanUniverseFile();
        // Verlet v = new Verlet();
        // Beeman b = new Beeman();
        // GearPredictorCorrector gpc = new GearPredictorCorrector();
        // OsciladorAmortiguado.simulate(v,verletFile);
        // OsciladorAmortiguado.simulate(b, beemanFile);
        // OsciladorAmortiguado.simulate(gpc, gpcFile);
        runElectricEj2();
        // OsciladorAmortiguado.calculateAnalyticSolution(analyticFile);
        // ElectricUniverse eu = new ElectricUniverse();
        // eu.simulate(b, electricFile);
    }

    public static void runElectricEj1(){
        double dt1 = 1e-13;
        double dt2 = 1e-14;
        double dt3 = 1e-15;
        double dt4 = 1e-16;
    
        double pos1 = ElectricUniverse.randomY();
        double pos2 = ElectricUniverse.randomY();
        double pos3 = ElectricUniverse.randomY();
        double pos4 = ElectricUniverse.randomY();
        double pos5 = ElectricUniverse.randomY();
    
        double[] pos_array = {pos1, pos2, pos3, pos4, pos5};
        double[] dts_array = {dt1, dt2, dt3, dt4};

        Beeman b = new Beeman();
        ElectricUniverse eu = new ElectricUniverse();
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                
                String filename = "Ej1/OutputPos_" + j + "_Dt_" + i + ".csv";
                eu.simulate(b, filename, dts_array[i-1], 5 * dts_array[i-1], pos_array[j-1]);
            }
        }
    }

    public static void runElectricEj2(){
        double v1 = ElectricUniverse.randomVx();
        double v2 = ElectricUniverse.randomVx();
        double v3 = ElectricUniverse.randomVx();
        double v4 = ElectricUniverse.randomVx();
        double v5 = ElectricUniverse.randomVx();
        double[] velocities = {v1,v2,v3,v4,v5};

        double dt = 1e-16;
        String filename = "OutputElectricEj2.csv";
        Beeman b = new Beeman();
        ElectricUniverse eu = new ElectricUniverse();
        OutputParser.createCleanPythonFile(filename);
        Pair<Double, Integer> pair = null;
        for (int i = 0; i < velocities.length; i++) {
            List<Double> trajectory = new ArrayList<>();
            List<Integer> endType = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                double y0 = ElectricUniverse.randomY();
                pair = eu.simulateEj2(b, dt, y0, velocities[i]);
                trajectory.add(pair.first);
                endType.add(pair.second);
            }
            OutputParser.OutputEj2(filename, velocities[i], trajectory, endType);
        }
    }
}

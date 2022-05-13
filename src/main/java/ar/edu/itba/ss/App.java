package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.*;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.parsers.*;

public class App {

    public static void main(String[] args) {
        // OutputParser.createCleanUniverseFile();
        
        // OsciladorAmortiguado.simulate(v,verletFile);
        // OsciladorAmortiguado.simulate(b, beemanFile);
        // OsciladorAmortiguado.simulate(gpc, gpcFile);
        // runElectricEj1();
        // runOscilatory();
        runElectricEj2();
        // OsciladorAmortiguado.calculateAnalyticSolution(analyticFile);
        // ElectricUniverse eu = new ElectricUniverse();
        // eu.simulate(b, electricFile);
    }

    public static void runOscilatory(){
        double dt1 = 1e-2;
        double dt2 = 1e-3;
        double dt3 = 1e-4;
        double dt4 = 1e-5;

        double[] dts = {dt1,dt2,dt3,dt4};

        Verlet v = new Verlet();
        Beeman b = new Beeman();
        GearPredictorCorrector gpc = new GearPredictorCorrector();
        for (int i = 0; i < dts.length; i++) {
            String vertelFn = "OscilatoryEj/OutputVertelDt_" + (i+1) + ".csv";
            String beemanFn = "OscilatoryEj/OutputBeemanDt_" + (i+1) + ".csv";
            String gpcFn = "OscilatoryEj/OutputGPCDt_" + (i+1) + ".csv";
            String analyticFn = "OscilatoryEj/OutputAnalyticDt_" + (i+1) + ".csv";
            OsciladorAmortiguado.simulate(v, vertelFn, dts[i]);
            OsciladorAmortiguado.simulate(b, beemanFn, dts[i]);
            OsciladorAmortiguado.simulate(gpc, gpcFn, dts[i]);
            OsciladorAmortiguado.calculateAnalyticSolution(analyticFn, dts[i]);
        }
    }

    public static void runElectricEj1(){
        double dt1 = 1e-13;
        double dt2 = 1e-14;
        double dt3 = 1e-15;
        double dt4 = 1e-16;
        // double dt5 = 1e-17;
        
        double pos1 = ElectricUniverse.randomY();
        double pos2 = ElectricUniverse.randomY();
        double pos3 = ElectricUniverse.randomY();
        double pos4 = ElectricUniverse.randomY();
        double pos5 = ElectricUniverse.randomY();
    
        double[] pos_array = {pos1, pos2, pos3, pos4, pos5};
        double[] dts_array = {dt1, dt2, dt3, dt4};

        for(double p : pos_array)
            System.out.println(p);

        Verlet v = new Verlet();
        ElectricUniverse eu = new ElectricUniverse();
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                String filename = "Ej1/OutputPos_" + j + "_Dt_" + i + ".csv";
                eu.simulate(v, filename, dts_array[i-1], 2 * dts_array[i-1], pos_array[j-1]);
            }
        }
    }

    public static void runElectricEj2(){
        double v1 = 10000;
        double v2 = 15000;
        double v3 = 25000;
        double v4 = 35000;
        double v5 = 45000;
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
            for (int j = 0; j < 50; j++) {
                String fn = "";
                String xyz = "";
                boolean parse = false;
                if(j % 5 == 0 && j<20){
                    fn = "Trajectories/OutputTrajectoriesV" + (i+1) + "_" + j + ".csv";
                    // xyz = "XYZ/OutputTrajectoriesV" + (i+1) + "_" + j + ".xyz";
                    parse = true;
                }
                double y0 = ElectricUniverse.randomY();
                pair = eu.simulateEj2(b, dt, y0, velocities[i], fn, parse,xyz);
                trajectory.add(pair.first);
                endType.add(pair.second);
            }
            OutputParser.OutputEj2(filename, velocities[i], trajectory, endType);
        }
    }
}

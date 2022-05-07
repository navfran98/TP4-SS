package ar.edu.itba.ss;
import ar.edu.itba.ss.models.*;
import ar.edu.itba.ss.utils.*;
import ar.edu.itba.ss.parsers.*;

public class App {

    private static final String verletFile = "OutputVerlet.csv";
    private static final String beemanFile = "OutputBeeman.csv";
    private static final String gpcFile = "OutputGPC.csv";
    private static final String analyticFile = "OutputAnalyticSolution.csv";
    private static final String electricFile = "OutputElectric.csv";
    public static void main(String[] args) {
        OutputParser.createCleanUniverseFile();
        Verlet v = new Verlet();
        Beeman b = new Beeman();
        GearPredictorCorrector gpc = new GearPredictorCorrector();
        // OsciladorAmortiguado.simulate(v,verletFile);
        // OsciladorAmortiguado.simulate(b, beemanFile);
        // OsciladorAmortiguado.simulate(gpc, gpcFile);
        // OsciladorAmortiguado.calculateAnalyticSolution(analyticFile);
        ElectricUniverse eu = new ElectricUniverse();
        eu.simulate(b, electricFile);
    }
}

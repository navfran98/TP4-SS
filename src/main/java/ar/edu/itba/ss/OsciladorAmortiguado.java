package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Pair;
import ar.edu.itba.ss.parsers.OutputParser;
import ar.edu.itba.ss.utils.Verlet;

public class OsciladorAmortiguado {

    private static final Double m = 70.0;
    private static final Double k = 10000.0;
    private static final Double gamma = 100.0;
    private static final Double tf = 5.0;

    private static final Double A = 2.0;

    private static final Double r0 = 1.0;
    private static final Double v0 = -A * gamma / (2*m);

    private static final Double dt = 0.01;


    public static void main(String[] args) {

        OutputParser.createCleanPythonFile();

        double x = r0;
        double v = v0;

        // Paso en t=0
        double f = (-k * x) - (gamma * v);
        OutputParser.writePythonCSV(f, x, v, 0);

        for (double t = 0.0 + dt; t <= tf; t += dt) {
            Pair<Double, Double> rta = Verlet.getNextPos1D(x, v, m, t, dt, f);
            x = rta.first;
            v = rta.second;
            f = (-k * x) - (gamma * v);
            OutputParser.writePythonCSV(f, x, v, t);
        }

    }
}

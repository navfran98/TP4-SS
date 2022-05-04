package ar.edu.itba.ss.parsers;

import ar.edu.itba.ss.models.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OutputParser {

    private static boolean first = true;
    private static final String filename = "OutputPython.csv";
    private static final String filenameUniverse = "OutputTP4.xyz";

    public static void writePythonCSV(double f, double x, double v, double t) {
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("T,X,V,F\n");
                first=false;
            }
            dump.append(t).append(",").append(x).append(",").append(v).append(",").append(f).append("\n");
            appendToEndOfFile(filename, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeUniverse(List<Particle> particles) {
        try {
            StringBuilder dump = new StringBuilder(particles.size() + "\n" + "Time=" + 0 + "\n");
            for (Particle p : particles) {
                int color;
                if(p.getCharge() > 0) {
                    color = 200;
                } else {
                    color = 0;
                }
                dump.append(color).append(" ");
                dump.append(p.getX()).append(" ")
                        .append(p.getY()).append(" ")
                        .append(1e-8 / 10).append(" ")
                        .append(p.getVx()).append(" ")
                        .append(p.getVy()).append(" \n");
            }
            appendToEndOfFile(filenameUniverse,dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void appendToEndOfFile(String file,String text) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.close();
    }

    public static void createCleanUniverseFile() {
        Path fileToDeletePath = Paths.get(filenameUniverse);
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createCleanPythonFile() {
        Path fileToDeletePath = Paths.get(filename);
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

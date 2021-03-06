package ar.edu.itba.ss.parsers;

import ar.edu.itba.ss.models.Force;
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
    private static boolean first_trajectory = true;
    private static final String filenameUniverse = "OutputTP4.xyz";

    public static void writePythonCSV(Force f, double x, double v, double t, String fn) {
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("T,X,V,F\n");
                first=false;
            }
            dump.append(t).append(",").append(x).append(",").append(v).append(",").append(f.getX()).append("\n");
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writePythonAnalyticCSV(double t,double x, String fn) {
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("T,X\n");
                first=false;
            }
            dump.append(t).append(",").append(x).append("\n");
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeElectricPythonCSV(Force f, double x, double y, double vx, double vy, double t, String fn) {
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("T,X,Y,Vx,Vy,Fx,Fy\n");
                first=false;
            }
            dump.append(t).append(",").append(x).append(",").append(y).append(",").append(vx).append(",").append(vy).append(",").append(f.getX()).append(",").append(f.getY()).append("\n");
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeUniverse(String fn, Particle fp, List<Particle> particles) {
        try {
            int color;
            StringBuilder dump = new StringBuilder(particles.size() + 1 + "\n" + "Time=" + 0 + "\n");
            if(fp.getCharge() > 0) {
                color = 200;
            } else {
                color = 0;
            }
            dump.append(color).append(" ");
            dump.append(fp.getX()).append(" ")
                    .append(fp.getY()).append(" ")
                    .append(1e-8 / 10).append(" ")
                    .append(fp.getVx()).append(" ")
                    .append(fp.getVy()).append(" \n");
            for (Particle p : particles) {
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
            appendToEndOfFile(fn,dump.toString());
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

    public static void createCleanUniverseFile(String fn) {
        Path fileToDeletePath = Paths.get(fn);
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createCleanPythonFile(String fn) {
        Path fileToDeletePath = Paths.get(fn);
        first = true;
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createCleanPythonTrajectoryFIle(String fn) {
        Path fileToDeletePath = Paths.get(fn);
        first_trajectory = true;
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void OutputEj1(String fn, double t, double total_energy){
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("T,E\n");
                first=false;
            }
            dump.append(t).append(",").append(total_energy).append("\n");
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void OutputEj2(String fn, double v, List<Double> trajectories, List<Integer> endTypes){
        try {
            StringBuilder dump = new StringBuilder();
            if(first){
                dump.append("V,Tra,End\n");
                first=false;
            }
            for (int i = 0; i < trajectories.size(); i++) {
                dump.append(v).append(",").append(trajectories.get(i)).append(",").append(endTypes.get(i)).append("\n");
            }
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void parseTrajectory(String fn, double x, double y){
        try {
            StringBuilder dump = new StringBuilder();
            if(first_trajectory){
                dump.append("X,Y\n");
                first_trajectory=false;
            }
            dump.append(x).append(",").append(y).append("\n");
            appendToEndOfFile(fn, dump.toString());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

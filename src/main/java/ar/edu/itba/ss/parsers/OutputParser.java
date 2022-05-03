package ar.edu.itba.ss.parsers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputParser {

    private static boolean first = true;
    private static final String filename = "OutputPython.csv";

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

    private static void appendToEndOfFile(String file,String text) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.close();
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

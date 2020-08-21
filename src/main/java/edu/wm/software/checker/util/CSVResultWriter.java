package edu.wm.software.checker.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVResultWriter {
    
    private final String csv_file_name;
    
    private List<String> linesToWrite = new ArrayList();
    
    public CSVResultWriter(String CSVName){
        csv_file_name = ResourceLoader.getAbsolutePath() + CSVName;
    }
    public void buildCSVResult() {
        File csvOutputFile = new File(csv_file_name);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            linesToWrite.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    public void addResultLines(List<String> lines) {
        linesToWrite.addAll(lines);
    }
    public void addResultLine(String line) {
        linesToWrite.add(line);
    }
}

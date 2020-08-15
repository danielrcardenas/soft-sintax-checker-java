package edu.wm.software.checker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSV2ClassFileConverter {
    
    public static final String DATASET = "dataset";
    
    private static  final String CLASS_TEMPLATE = "public class Test { <code> } "; //TODO DRC: this shpuld be replaces by a template
    public static void createClassFiles(String csvFile) {
        String absolutePath = ResourceLoader.getAbsolutePath();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(absolutePath + csvFile));
            String row;
            csvReader.readLine(); //header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String className = "Test" + data[0];
                String classCode =  CLASS_TEMPLATE.replace("Test", className);
                String code = data[1].replace("\"", "");
                classCode = classCode.replace("<code>", code);
                createClassFile(classCode, className);
            }
            csvReader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    protected static void createClassFile(String code, String className){
    
        try (FileWriter classWriter = new FileWriter(ResourceLoader.getAbsolutePath() + DATASET+ File.separator + className + ".java")) {
            classWriter.append(code);
            classWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

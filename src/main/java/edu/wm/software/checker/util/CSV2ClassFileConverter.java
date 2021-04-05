package edu.wm.software.checker.util;

import static java.lang.Integer.parseInt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CSV2ClassFileConverter {
    
    public static final String DATASET = "compiled";
    
    public static final String CSV_SEPARATOR = ",";
    
    public static final  String STOP_WORD = "*stop*";
    
    private static  final String CLASS_TEMPLATE = "public class Test { <code> }"; //TODO DRC: this should be replaces by a template
    public static void createClassFiles(String csvFile) {
        String absolutePath = ResourceLoader.getAbsolutePath();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(absolutePath + csvFile));
            String row;
            
            StringBuilder method = new StringBuilder();
            String className = null;
            String rowBefore = STOP_WORD; //header
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                if (isNewMethod(row, rowBefore)) {
                    createClassFile(method.toString(), className);
                    method = new StringBuilder();
                    String[] data = row.split(CSV_SEPARATOR, 2);
                    className = "Test" + data[0];
                    String code = cleanData(data[1]);
                    method.append(code);
                }else {
                    method.append("\n");
                    method.append(cleanData(row));
                }
                rowBefore = row;
            }
            createClassFile(method.toString(), className);
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
    
        if (code == "" ||className == null) {
            return;
        }
        String classCode =  CLASS_TEMPLATE.replace("Test", className);
        classCode = classCode.replace("<code>", code);
    
        try (FileWriter classWriter = new FileWriter(ResourceLoader.getAbsolutePath() + DATASET+ File.separator + className + ".java", false)) {
            classWriter.append(classCode);
            classWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected static String cleanData(String code) {
        if (code.charAt(0) == '\"') {
            code = code.substring(1);
        }
        
        code = code.replace("\"\"", "\"");
        code = code.replace("<n>", "\n");
        code = code.replace("<t>", "\t");
        code = code.replace("<@>", "@");
        code = code.replace("\"," + STOP_WORD, "");
        code = code.replace("," + STOP_WORD, "");
        
        return code;
    }
    
    protected static boolean isNewMethod(String row, String rowBefore) {
       String [] result = rowBefore.split(CSV_SEPARATOR);
       if (result[result.length-1].contains(STOP_WORD)) {
           return true;
       }
       
       return false;
    }
    
    
    protected static boolean isNewMethodOld(String row, String rowBefore) {
        String [] result = row.split(CSV_SEPARATOR);
        try {
            int id = parseInt(result [0]);
            if(rowBefore.charAt(rowBefore.length() -1) == '\"') {
                return true;
            }
        }
        catch (NumberFormatException e) {
        
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }
    
}

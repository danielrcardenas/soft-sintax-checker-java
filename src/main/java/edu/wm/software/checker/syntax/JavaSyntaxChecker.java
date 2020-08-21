package edu.wm.software.checker.syntax;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import edu.wm.software.checker.util.CSV2ClassFileConverter;
import edu.wm.software.checker.util.CSVResultWriter;
import edu.wm.software.checker.util.ResourceLoader;

public class JavaSyntaxChecker {
    public static void main(String[] args) {
        CSV2ClassFileConverter.createClassFiles(args[0]);
        CSVResultWriter csvResultWriter = new CSVResultWriter(args[1]);
        String header = "ID Class\t Type error\t line\t position\t error message";
        csvResultWriter.addResultLine(header);
        for (File file : ResourceLoader.getFilesOnResourcePath()) {
            csvResultWriter.addResultLines(JavaSyntaxChecker.check(file));
        }
        
        csvResultWriter.buildCSVResult();
    }
    
    
    public static List<String> check(File file) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));
        
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();
        
        List<String> messages = new ArrayList<>();
        String id = file.getName().split("\\.")[0].replace("Test", "");
        
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            String result = String.join("\t", id, diagnostic.getKind().name(),
                    String.valueOf(diagnostic.getLineNumber()),
                    String.valueOf(diagnostic.getPosition()),
                    diagnostic.getMessage(Locale.ROOT));
            
            messages.add(result);
        }
        
        return messages;
    }
}
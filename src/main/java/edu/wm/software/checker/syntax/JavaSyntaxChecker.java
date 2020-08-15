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
import edu.wm.software.checker.util.ResourceLoader;

public class JavaSyntaxChecker {
    public static void main(String[] args) {
        CSV2ClassFileConverter.createClassFiles("lstm_java_samples.csv");
        for (File file : ResourceLoader.getFilesOnResourcePath()) {
            System.out.println(JavaSyntaxChecker.check(file));
        }
    }
    
    public static List<String> check(File file) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file));
        
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits).call();
        
        List<String> messages = new ArrayList<String>();
        Formatter formatter = new Formatter();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            messages.add(diagnostic.getKind() + ":\t Line [" + diagnostic.getLineNumber() + "] \t Position [" + diagnostic.getPosition() + "]\t" + diagnostic.getMessage(Locale.ROOT) + "\n");
        }
        
        return messages;
    }
}
package edu.wm.software.checker.util;

import static edu.wm.software.checker.util.CSV2ClassFileConverter.DATASET;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceLoader {
    
    private static String absolutePath = null;
    
    private static final String RELATIVE_PATH = "java/resources/";
    
    private static ResourceLoader instance = new ResourceLoader();
    
    private ResourceLoader() {
        File file = new File(RELATIVE_PATH + "abc.txt");
        
        absolutePath = file.getAbsolutePath().replace("abc.txt", "");
    }
    
    public static List<File> getFilesOnResourcePath() {
        try (Stream<Path> paths = Files.walk(Paths.get(absolutePath + DATASET))) {
            return paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String getAbsolutePath() {
        return absolutePath;
    }
    
    public static ResourceLoader getInstance() {
        return instance;
    }
}

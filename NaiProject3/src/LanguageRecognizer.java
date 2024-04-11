import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageRecognizer {
    public static void main(String[] args) {
        LanguageRecognizer lr = new LanguageRecognizer();
        System.out.println(lr.loadLanguages());

    }

    private Map<String, List<String>> loadLanguages(){
        Map<String, List<String>> languageMap = new HashMap<>();
        try {
            Files.walkFileTree(Paths.get("languages"), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        if(file..equals("Angielski")){

                        }
                        List<String> lines = Files.readAllLines(file);
                        languageMap.put(file.getFileName().toString().split("\\.")[0], lines);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return languageMap;
    }
}

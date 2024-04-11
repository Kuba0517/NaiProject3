import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LanguageRecognizer {
    private Map<String, Integer> languageToLabel = new HashMap<>();
    private Map<Integer, String> labelToLanguage = new HashMap<>();
    private Map<Integer, Perceptron> perceptrons = new HashMap<>();

    public LanguageRecognizer() {
        loadLanguagesAndTrainPerceptrons();
    }

    private void loadLanguagesAndTrainPerceptrons() {
        Map<String, List<String>> result = loadLanguages();

        List<double[]> vectors = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        int currentLabel = 0;
        for (String language : result.keySet()) {
            if (!languageToLabel.containsKey(language)) {
                languageToLabel.put(language, currentLabel);
                labelToLanguage.put(currentLabel, language);
                currentLabel++;
            }

            int label = languageToLabel.get(language);
            result.get(language).forEach(text -> {
                vectors.add(textToVector(text));
                labels.add(label);
            });
        }

        trainPerceptrons(vectors, labels);
    }

    private void trainPerceptrons(List<double[]> vectors, List<Integer> labels) {
        for (Integer label : languageToLabel.values()) {
            Perceptron perceptron = new Perceptron();
            List<Integer> correctLabel = labels.stream().map(e -> e == label ? 1 : 0).toList();
            perceptron.trenuj(vectors, correctLabel);
            perceptrons.put(label, perceptron);
        }
    }
    public Map<String, List<String>> loadLanguages(){
        Map<String, List<String>> languageMap = new HashMap<>();
        try {
            Files.walkFileTree(Paths.get("languages"), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        String[] stringPath = file.toString().split("\\\\");
                        List<String> lines = Files.readAllLines(file);
                        String stringLines = filterStringLiterals(lines.toString());
                        languageMap.computeIfAbsent(stringPath[1], v -> new ArrayList<>());
                        languageMap.get(stringPath[1]).add(stringLines);
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

    public String filterStringLiterals(String text){
        IntStream charStream = text.chars().filter(e -> (e >= 65 && e <= 90) || (e >= 97 && e <= 122));
        StringBuilder result = new StringBuilder();
        charStream.forEach(e -> result.append((char) e));
        return result.toString();
    }

    public double[] textToVector(String text) {
        double[] vector = new double[26];
        text = text.toLowerCase();
        int totalLetters = 0;

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                vector[c - 'a']++;
                totalLetters++;
            }
        }

        for (int i = 0; i < vector.length; i++) {
            vector[i] = totalLetters > 0 ? vector[i] / totalLetters : 0;
        }

        return vector;
    }

    public String classifyText(String text) {
        double[] vector = textToVector(text);
        int bestLabel = -1;
        double bestScore = -Double.MAX_VALUE;

        for (Map.Entry<Integer, Perceptron> entry : perceptrons.entrySet()) {
            int label = entry.getKey();
            Perceptron perceptron = entry.getValue();
            double score = perceptron.klasyfikacja(vector);

            if (score > bestScore) {
                bestScore = score;
                bestLabel = label;
            }
        }

        return labelToLanguage.get(bestLabel);
    }

}

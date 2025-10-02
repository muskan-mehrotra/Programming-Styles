import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            //prompt for user to enter correct input file in case of missing input
            System.out.println("Usage: java Main <inputBook>");
            return;
        }

        String inputBook = args[0];
        String stopWordsFile = "stop_words.txt";

        try {
            Set<String> stopWords = new HashSet<>();
            String stopWordsText = Files.readString(Paths.get(stopWordsFile)).toLowerCase();
            for (String w : stopWordsText.split("[,\\s]+")) {
                if (!w.isBlank()) stopWords.add(w);
            }

            String inputText = Files.readString(Paths.get(inputBook)).toLowerCase();

            // Normalize common abbreviations before removing punctuation
            inputText = inputText.replaceAll("\\bmr\\b\\.?", "mr");
            inputText = inputText.replaceAll("\\bmrs\\b\\.?", "mrs");
            inputText = inputText.replaceAll("\\bms\\b\\.?", "ms");

            // Normalizing quotes and apostrophes
            inputText = inputText.replaceAll("[’‘“”]", "'"); 

            // Remove remaining punctuation, keep only letters and spaces
            inputText = inputText.replaceAll("[^a-z\\s]", " ");

            String[] words = inputText.split("\\s+");

            // Count frequencies using HashMap approach
            Map<String, Integer> freqWordMap = new HashMap<>();
            for (String w : words) {
                if (w.isBlank() || stopWords.contains(w)) continue;

                // Remove trailing "'s" to count base words
                if (w.endsWith("'s")) w = w.substring(0, w.length() - 2);
                w = w.trim();
                if (w.isBlank() || stopWords.contains(w)) continue;

                freqWordMap.put(w, freqWordMap.getOrDefault(w, 0) + 1);
            }

            //Sorting in order to display most frequent word on top
            List<Map.Entry<String, Integer>> topList = freqWordMap.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(25)
                    .collect(Collectors.toList());

            System.out.println("Top 25 words in " + inputBook + ":");
            for (Map.Entry<String, Integer> entry : topList) {
                System.out.printf("%-10s -  %d%n", entry.getKey(), entry.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import java.io.*;
import java.nio.file.*;
import java.util.*;

class TermFreq {
    String word;
    int count;

    TermFreq(String word) {
        this.word = word;
        this.count = 1;
    }
}

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <bookFile path>");
            return;
        }

        String bookFile = args[0];
        String stopWordsFile = "/Users/muskan20mehrotra/Documents/MSWE/Programming_Styles/stop_words.txt"; // fixed stop words file

        try {
            List<String> stopWords = new ArrayList<>();
            String stopWordsText = Files.readString(Paths.get(stopWordsFile)).toLowerCase();
            for (String w : stopWordsText.split("[,\\s]+")) {
                if (!w.isBlank()) stopWords.add(w);
            }

            String text = Files.readString(Paths.get(bookFile)).toLowerCase();
            text = text.replaceAll("[^a-z\\s]", " ");
            //removing punctuation and special characters
            String[] words = text.split("\\s+");

            List<TermFreq> freqList = new ArrayList<>();

            for (String w : words) {
                if (w.isBlank() || w.equals("s") || stopWords.contains(w)) continue;
                //to skip the trailing "s" and stop words

                boolean found = false;
                for (TermFreq wf : freqList) {
                    if (wf.word.equals(w)) {
                        wf.count++;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    freqList.add(new TermFreq(w));
                }
            }

            freqList.sort((a, b) -> b.count - a.count);

            System.out.println("Top 25 words in " + bookFile + ":");
            for (int i = 0; i < Math.min(25, freqList.size()); i++) {
                System.out.printf("%-10s %d%n", freqList.get(i).word, freqList.get(i).count);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

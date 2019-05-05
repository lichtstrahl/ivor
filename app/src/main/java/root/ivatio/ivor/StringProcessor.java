package root.ivatio.ivor;

import java.util.ArrayList;
import java.util.List;

import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.app.App;

public class StringProcessor {
    private StringProcessor() {
        throw new IllegalStateException("Это вспомогательный класс. Создание экземпляра не требуется.");
    }

    public static String stringDeleteChars(String str, String chars) {
        return str.replaceAll(chars, "");
    }

    public static List<KeyWord> getKeyWords(String string) {
        ArrayList<KeyWord> list = new ArrayList<>();
        for (KeyWord word : App.getStorageAPI().getKeyWords())
            if (string.contains(word.content))
                list.add(word);
        return list;
    }

    public static List<KeyWord> getAllocatedKeyWords(String string) {
        ArrayList<KeyWord> words = new ArrayList<>();

        // Тройным знаком "#" выделяются ключевые слова
        for (String s : string.split(" ")) {
            if (isKeyWord(s)) {
                char keyWords[] = new char[s.length()-3];
                s.getChars(3, s.length()-1, keyWords, 0);
                words.add(new KeyWord(String.valueOf(keyWords)));
            }
        }
        return words;
    }

    private static boolean isKeyWord(String s) {
        return s.length() > 3 && s.charAt(0) == '#' && s.charAt(1) == '#' && s.charAt(2) == '#';
    }

    public static String toStdFormat(String str) {
        return stringDeleteChars(str, ",|!|\\.|\\?")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
package root.ivatio.util;

import java.util.ArrayList;
import java.util.List;

import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.App;

public class StringProcessor {
    private StringProcessor() {
        throw new IllegalStateException("Это вспомогательный класс. Создание экземпляра не требуется.");
    }

    public static String stringDeleteChars(String str, String chars) {
        return str.replaceAll(chars, "");
    }

    public static List<KeyWord> getKeyWords(String string, List<KeyWord> words) {
        ArrayList<KeyWord> list = new ArrayList<>();
        for (KeyWord word : words)
            if (string.contains(word.content))
                list.add(word);
        return list;
    }

    public static String toStdFormat(String str) {
        return stringDeleteChars(str, ",|!|\\.|\\?")
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
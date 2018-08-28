package Ivor;

import java.util.ArrayList;

import bd.KeyWord.KeyWord;
import root.ivatio.App;

public class StringProcessor {
    public boolean stringContainsWord(String str, String word) {
        return str.contains(word);
    }

    public String stringDeleteChars(String str, String chars) {
        return str.replaceAll(chars, "");
    }

    // Насколько совпадают строки. В прцоентах
    public double compareString(String s1, String s2) {
        int n = Math.min(s1.length(), s2.length());
        if (n == 0)
            return 0;
        int k = 0;
        for (int i = 0; i < n; i++)
            if (s1.charAt(i) == s2.charAt(i))
                k++;
        return k/Math.max(s1.length(), s2.length());
    }

    public ArrayList<KeyWord> getKeyWords(String string) {
        ArrayList<KeyWord> list = new ArrayList<>();
        for (KeyWord word : App.getDB().getKeyWordDao().getAll())
            if (string.contains(word.content))
                list.add(word);
        return list;
    }
}

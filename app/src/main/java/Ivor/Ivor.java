package Ivor;

// Основнок класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;

import bd.Answer.Answer;
import bd.KeyWord.KeyWord;
import bd.Users.User;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;

import static root.ivatio.App.getDB;

public class Ivor extends User {
    public static String Name = "Ivor";
    private Resources resources;
    private StringProcessor stringProcessor;

    public Ivor(Resources resources) {
        this.id = -1;
        this.resources = resources;
        this.stringProcessor = new StringProcessor();
    }

    public String processingMessage(String message) {
        String liteString = stringProcessor.stringDeleteChars(message, ",|!|\\.|\\?");
        ArrayList<KeyWord> keyWords = stringProcessor.getKeyWords(liteString);

        if (keyWords.size() != 0) {
            Answer answer = getDB().getAnswerDao().getAnswer(keyWords.get(0).answerID);
            if (answer != null)
                return answer.content;
        }

        return resources.getString(R.string.noAnswer);
    }

    public Message send(String content) {
        return new Message(null, processingMessage(content), Calendar.getInstance().getTime());
    }

    public double compare(String str1, String str2) {
        int n = str2.length();
        int l = Math.min(str1.length(), str2.length());
        int not = 0;
        for (int i = 0; i < l; i++)
            if (str1.charAt(i) != str2.charAt(i))
                not++;
        return Math.round(not/n * 100);
    }
}

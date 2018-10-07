package ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bd.answer.Answer;
import bd.communication_key.CommunicationKey;
import bd.key_word.KeyWord;
import bd.users.User;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;

public class Ivor extends User {
    private static String name = "Ivor";
    private Resources resources;
    private StringProcessor stringProcessor;
    private ArrayList<KeyWord> memoryWords;
    private Random random;

    public Ivor(Resources resources) {
        this.id = -1;
        this.resources = resources;
        this.stringProcessor = new StringProcessor();
        this.memoryWords = new ArrayList<>();
        this.random = new Random();
    }

    public static String getName() {
        return name;
    }

    private String processingMessage(String message) {
        String liteString = stringProcessor
                                .stringDeleteChars(message, ",|!|\\.|\\?")
                                .toLowerCase()
                                .trim()
                                .replaceAll("\\s+", " ");

        return processingKeyWords(stringProcessor.getKeyWords(liteString));
    }
    private String processingKeyWords(ArrayList<KeyWord> keyWords) {
        for (KeyWord word : keyWords) {
            List<CommunicationKey> list = App.getDB().getCommunicationKeyDao().getCommunications(word.id);
            if (list.isEmpty())
                continue;
            int r = random.nextInt(list.size());
            Answer answer = App.getDB().getAnswerDao().getAnswer(list.get(r).answerID);
            if (answer != null) {
                memoryWords.add(word);
                return answer.content;
            }
        }

        return resources.getString(R.string.ivorNoAnswer);
    }

    public Message answer(String request) {
        return send(processingMessage(request));
    }

    public Message send(String content) {
        return new Message(null, content);
    }

    public Message send(int res) {
        return new Message(null, resources.getString(res));
    }


    public double compare(String str1, String str2) {
        int n = str2.length();
        int l = Math.min(str1.length(), str2.length());
        int not = 0;
        for (int i = 0; i < l; i++)
            if (str1.charAt(i) != str2.charAt(i))
                not++;
        return Math.round((double)not / (double)n * 100.0);
    }

    public void reEvaluationKeyWord(int eval, Answer answer) {
        KeyWord keyWord = getLastKeyWord();
        CommunicationKey communicationKey = App.getDB().getCommunicationKeyDao().getCommunicationKey(keyWord.id, answer.id);
        if (communicationKey != null) {
            communicationKey.power++;
            if (eval > 0)
                communicationKey.correct++;
            if (eval < 0)
                communicationKey.correct--;
            App.getDB().getCommunicationKeyDao().update(communicationKey);
        }
    }

    public KeyWord getLastKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(memoryWords.size()-1);
    }

    public KeyWord getFirstKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(0);
    }

    public boolean haveKeyWords() {
        return !memoryWords.isEmpty();
    }
    public Message ivorNowAsking() {
        return send(resources.getString(R.string.ivorModeIvorNowAsking));
    }
    public Message userNowAsking() {
        return send(resources.getString(R.string.ivorModeUserNowAsking));
    }
    public Message successfulAnswer() {
        return send(resources.getString(R.string.ivorSuccessfulAnswer));
    }

    public Message getRandomKeyWord() {
        long[] allID = App.getDB().getKeyWordDao().getAllID();
        long anyID = allID[new Random().nextInt(allID.length)];
        KeyWord keyWord = App.getDB().getKeyWordDao().getWord(anyID);
        memoryWords.add(keyWord);
        return new Message(null, keyWord.content);
    }
}

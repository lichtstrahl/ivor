package ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import bd.answer.Answer;
import bd.communication.Communication;
import bd.communication_key.CommunicationKey;
import bd.key_word.KeyWord;
import bd.qustion.Question;
import bd.users.User;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;
import root.ivatio.activity.MsgActivity;

public class Ivor extends User {
    private static String name = "Ivor";
    private Resources resources;
    private ArrayList<KeyWord>  memoryWords;
    private ArrayList<Question> memoryQuestions;
    private ArrayList<Answer>   memoryAnswers;
    private ArrayList<bd.Communication> memoryCommunications;
    private Random random;
    private boolean processingKeyWord;
    private boolean processingQuestion;

    public Ivor(Resources resources) {
        this.id = -1;
        this.resources = resources;
        this.memoryWords = new ArrayList<>();
        this.memoryQuestions = new ArrayList<>();
        this.memoryAnswers = new ArrayList<>();
        this.memoryCommunications = new ArrayList<>();
        this.random = new Random();
        processingKeyWord = false;
    }

    public static String getName() {
        return name;
    }

    private String processingMessage(String message) {
        String liteString = StringProcessor.toStdFormat(message);
        Question q = isQuestion(liteString);
        if (q != null)  // Обработка прямого вопроса
            return processingQuestion(q);
        else            // Обработка KeyWord
            return processingKeyWords(StringProcessor.getKeyWords(liteString));
    }

    private Question isQuestion(String str) {
        String []words = str.split(" ");
        HashSet<String> set = new HashSet<>(Arrays.asList(words));
        // Обработка Question
        List<Question> questions = App.getDB().getQuestionDao().getAll();
        for (Question q : questions) {
            String[] qwords = q.content.split(" ");
            HashSet<String> qSet = new HashSet<>(Arrays.asList(qwords));
            if (qSet.equals(set))
                return q;
        }
        return null;
    }

    // TODO То, что здесь происходит, должно решаться через JOIN!
    private String processingQuestion(Question q) {
        processingKeyWord = false;
        List<Communication> communications = App.getDB().getCommunicationDao().getCommunication(q.id);
        if (!communications.isEmpty()) {
            int r = new Random().nextInt(communications.size());
            memoryQuestions.add(q);
            Communication com = communications.get(r);
            Answer answer = App.getDB().getAnswerDao().getAnswer(com.answerID);
            memoryCommunications.add(com);
            memoryAnswers.add(answer);
            processingQuestion = true;
            return answer.content;
        }

        processingQuestion = false;
        return resources.getString(R.string.ivorNoAnswer);
    }



    // TODO То, что здесь происходит, должно решаться через JOIN!
    private String processingKeyWords(ArrayList<KeyWord> keyWords) {
        processingQuestion = false;
        for (KeyWord word : keyWords) {
            List<CommunicationKey> list = App.getDB().getCommunicationKeyDao().getCommunications(word.id);
            if (list.isEmpty())
                continue;
            int r = random.nextInt(list.size());
            CommunicationKey com = list.get(r);
            Answer answer = App.getDB().getAnswerDao().getAnswer(com.answerID);
            if (answer != null) {
                processingKeyWord = true;
                memoryWords.add(word);
                memoryAnswers.add(answer);
                memoryCommunications.add(com);
                return answer.content;
            }
        }

        processingKeyWord = false;
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

    public void reEvaluationKeyWord(int eval) {
        Answer answer = getLastAnswer();
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
    public void reEvalutionQuestion(int eval) {
        Answer answer = getLastAnswer();
        Question question = getLastQuestion();
        Communication communication = App.getDB().getCommunicationDao().getCommunication(question.id, answer.id);
        if (communication != null) {
            communication.power++;
            if (eval > 0)
                communication.correct++;
            if (eval < 0)
                communication.correct--;
            App.getDB().getCommunicationDao().update(communication);
        }
    }


    public KeyWord getLastKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(memoryWords.size()-1);
    }
    public Question getLastQuestion() {
        if (memoryQuestions.isEmpty())
            return null;
        return memoryQuestions.get(memoryQuestions.size()-1);
    }

    public Answer getLastAnswer() {
        if (memoryAnswers.isEmpty())
            return null;
        return memoryAnswers.get(memoryAnswers.size()-1);
    }

    public bd.Communication getLastCommunication() {
        if (memoryCommunications.isEmpty())
            return null;
        return memoryCommunications.get(memoryCommunications.size()-1);
    }


    public KeyWord getFirstKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(0);
    }

    public boolean haveKeyWords() {
        return !memoryWords.isEmpty();
    }

    public Message sendRandomKeyWord() {
        KeyWord r = getRandomKeyWord();
        memoryWords.add(r);
        return new Message(null, r.content);
    }
    public KeyWord getRandomKeyWord() {
        long[] allID = App.getDB().getKeyWordDao().getAllID();
        long anyID = allID[new Random().nextInt(allID.length)];
        return App.getDB().getKeyWordDao().getWord(anyID);
    }

    public Message sendRandomQuestion() {
        Question r = getRandomQuestion();
        memoryQuestions.add(r);
        return new Message(null, r.content);
    }
    public Question getRandomQuestion() {
        long[] allID = App.getDB().getQuestionDao().getAllID();
        long anyID = allID[new Random().nextInt(allID.length)];
        return App.getDB().getQuestionDao().getQuestion(anyID);
    }

    public boolean processingKeyWord() {
        return processingKeyWord;
    }
    public boolean processingQuestion() {
        return processingQuestion;
    }

    public static class StringProcessor {
        public static boolean stringContainsWord(String str, String word) {
            return str.contains(word);
        }
        public static String stringDeleteChars(String str, String chars) {
            return str.replaceAll(chars, "");
        }

        // Насколько совпадают строки. В прцоентах
        public static double compareString(String s1, String s2) {
            int n = Math.min(s1.length(), s2.length());
            if (n == 0)
                return 0;
            int k = 0;
            for (int i = 0; i < n; i++)
                if (s1.charAt(i) == s2.charAt(i))
                    k++;
            return k/Math.max(s1.length(), s2.length());
        }

        public static ArrayList<KeyWord> getKeyWords(String string) {
            ArrayList<KeyWord> list = new ArrayList<>();
            for (KeyWord word : App.getDB().getKeyWordDao().getAll())
                if (string.contains(word.content))
                    list.add(word);
            return list;
        }

        public static ArrayList<KeyWord> getAllocatedKeyWords(String string) {
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
}

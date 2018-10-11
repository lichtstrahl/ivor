package ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import bd.Command.Command;
import bd.CommunicationAPI;
import bd.answer.Answer;
import bd.communication.Communication;
import bd.communication_key.CommunicationKey;
import bd.key_word.KeyWord;
import bd.qustion.Question;
import bd.users.User;
import ivor.action.Action;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;

public class Ivor extends User {
    private static String name = "Ivor";
    public static final int criticalCountEval = 50;
    private Resources resources;
    private LinkedList<KeyWord> memoryWords;
    private LinkedList<Question> memoryQuestions;
    private LinkedList<Answer>   memoryAnswers;
    private LinkedList<CommunicationAPI> memoryCommunicationAPIS;
    private List<Action> actions;
    private int countEval;
    private Random random;
    private boolean processingKeyWord;
    private boolean processingQuestion;
    private Action curAction;

    public Ivor(Resources resources, Action ... actions) {
        this.id = -1;
        this.resources = resources;
        this.memoryWords = new LinkedList<>();
        this.memoryQuestions = new LinkedList<>();
        this.memoryAnswers = new LinkedList<>();
        this.memoryCommunicationAPIS = new LinkedList<>();
        this.random = new Random();
        processingKeyWord = false;
        countEval = 0;
        this.actions = Arrays.asList(actions);
        curAction = null;
    }

    public static String getName() {
        return name;
    }

    private String processingMessage(String message) {
        String liteString = StringProcessor.toStdFormat(message);
        if (curAction == null) {
            Command c = isCommand(liteString);
            if (c != null) // Обработка команды
                return processingCommand(c);
            Question q = isQuestion(liteString);
            if (q != null)  // Обработка прямого вопроса
                return processingQuestion(q);
            else            // Обработка KeyWord
                return processingKeyWords(StringProcessor.getKeyWords(liteString));
        } else {
            curAction.put(liteString);
            return curAction.next();
        }
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

    private Command isCommand(String str) {
        HashSet<String> set = new HashSet<>(Arrays.asList(str.split(" ")));
        List<Command> commands = App.getDB().getCommandDao().getAll();

        for (Command cmd : commands) {
            HashSet<String> cmdSet = new HashSet<>(Arrays.asList(cmd.cmd.split(" ")));
            if (set.containsAll(cmdSet))
                return cmd;
        }
        return null;
    }

    private String processingCommand(Command c) {
        for (Action action : actions)
            if (action.getCmd().equals(c.cmd)) {
                curAction = action;
                return action.next();
            }

        return resources.getString(R.string.noAction);
    }

    private String processingQuestion(Question q) {
        processingKeyWord = false;
        List<Answer> answers = App.getDB().getQuestionDao().getAnswerForQuestion(q.id);
        if (!answers.isEmpty()) {
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(q).memory(App.getDB().getCommunicationDao().getCommunication(q.id, answer.id));
            processingQuestion = true;
            return answer.content;
        }
        processingQuestion = false;
        return resources.getString(R.string.ivorNoAnswer);
    }

    private String processingKeyWords(List<KeyWord> keyWords) {
        processingQuestion = false;
        for (KeyWord word : keyWords) {
            List<Answer> answers = App.getDB().getKeyWordDao().getAnswerForKeyWord(word.id);
            if (answers.isEmpty())
                continue;
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(word).memory(App.getDB().getCommunicationKeyDao().getCommunicationKey(word.id, answer.id));
            processingKeyWord = true;
            return answer.content;
        }

        processingKeyWord = false;
        return resources.getString(R.string.ivorNoAnswer);
    }

    private Ivor memory(Question q) {
        memoryQuestions.add(q);
        return this;
    }
    private Ivor memory(Answer a) {
        memoryAnswers.add(a);
        return this;
    }
    private Ivor memory(KeyWord kw) {
        memoryWords.add(kw);
        return this;
    }
    private Ivor memory(CommunicationAPI api) {
        memoryCommunicationAPIS.add(api);
        return this;
    }

    @Nullable
    Message answer(String request) {
        return send(processingMessage(request));
    }

    Message send(String content) {
        if (content.isEmpty())
            return null;
        return new Message(null, content);
    }

    Message send(int res) {
        return new Message(null, resources.getString(res));
    }


    public int getCountEval() {
        return countEval;
    }

    public List<String> resetAction() {
        List<String> res = curAction.getParam();
        curAction = null;
        return res;
    }

    void reEvalutionKeyWord(int eval) {
        Answer answer = getLastAnswer();
        KeyWord keyWord = getLastKeyWord();
        CommunicationKey communicationKey = App.getDB().getCommunicationKeyDao().getCommunicationKey(keyWord.id, answer.id);
        if (communicationKey != null) {
            countEval++;
            communicationKey.power++;
            if (eval > 0)
                communicationKey.correct++;
            if (eval < 0)
                communicationKey.correct--;
            App.getDB().getCommunicationKeyDao().update(communicationKey);
        }
    }
    void reEvalutionQuestion(int eval) {
        Answer answer = getLastAnswer();
        Question question = getLastQuestion();
        Communication communication = App.getDB().getCommunicationDao().getCommunication(question.id, answer.id);
        if (communication != null) {
            countEval++;
            communication.power++;
            if (eval > 0)
                communication.correct++;
            if (eval < 0)
                communication.correct--;
            App.getDB().getCommunicationDao().update(communication);
        }
    }


    private KeyWord getLastKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(memoryWords.size()-1);
    }
    private Question getLastQuestion() {
        if (memoryQuestions.isEmpty())
            return null;
        return memoryQuestions.get(memoryQuestions.size()-1);
    }

    private Answer getLastAnswer() {
        if (memoryAnswers.isEmpty())
            return null;
        return memoryAnswers.get(memoryAnswers.size()-1);
    }

    private CommunicationAPI getLastCommunication() {
        if (memoryCommunicationAPIS.isEmpty())
            return null;
        return memoryCommunicationAPIS.get(memoryCommunicationAPIS.size()-1);
    }

    Message sendRandomKeyWord() {
        KeyWord r = getRandomKeyWord();
        memoryWords.add(r);
        return new Message(null, r.content);
    }
    KeyWord getRandomKeyWord() {
        long[] allID = App.getDB().getKeyWordDao().getAllID();
        long anyID = allID[random.nextInt(allID.length)];
        return App.getDB().getKeyWordDao().getWord(anyID);
    }

    Message sendRandomQuestion() {
        Question r = getRandomQuestion();
        memoryQuestions.add(r);
        return new Message(null, r.content);
    }
    Question getRandomQuestion() {
        long[] allID = App.getDB().getQuestionDao().getAllID();
        long anyID = allID[random.nextInt(allID.length)];
        return App.getDB().getQuestionDao().getQuestion(anyID);
    }

    boolean processingKeyWord() {
        return processingKeyWord;
    }
    boolean processingQuestion() {
        return processingQuestion;
    }
    void deleteLastKeyWord() {
        App.getDB().getKeyWordDao().delete(getLastKeyWord());
    }
    void deleteLastQuestion() {
        App.getDB().getQuestionDao().delete(getLastQuestion());
    }

    /** Удаление не только Communication, но и CommunicationKey, если она была последней*/
    void deleteLastCommunication() {
        CommunicationAPI lastCom = getLastCommunication();
        if(lastCom.getType() == CommunicationAPI.COMMUNICATION)
            App.getDB().getCommunicationDao().delete(lastCom.getID());
        else
            App.getDB().getCommunicationKeyDao().delete(lastCom.getID());
    }

    void appendNewAnswerForLastKW(Answer answer) {
        long answerID = App.getDB().getAnswerDao().insert(answer);
        KeyWord lastKeyWord = getLastKeyWord();
        if (lastKeyWord == null)
            return;
        CommunicationKey comKey = new CommunicationKey(lastKeyWord.id, answerID);
        App.getDB().getCommunicationKeyDao().insert(comKey);
    }
    void appendNewAnswerForLastQ(Answer answer) {
        long answerID = App.getDB().getAnswerDao().insert(answer);
        Question lastQuestion = getLastQuestion();
        if (lastQuestion == null)
            return;
        Communication communication = new Communication(lastQuestion.id, answerID);
        App.getDB().getCommunicationDao().insert(communication);
    }
    boolean appendNewKeyWord(KeyWord keyWord) {
        List<KeyWord> keyWords = App.getDB().getKeyWordDao().getAll();
        if (!keyWords.contains(keyWord)) {
            App.getDB().getKeyWordDao().insert(keyWord);
            return  true;
        }
        return false;
    }
    boolean appendNewQuestion(Question question) {
        List<Question> questions = App.getDB().getQuestionDao().getAll();
        if (!questions.contains(question)) {
            App.getDB().getQuestionDao().insert(question);
            return true;
        }
        return false;
    }

    public void resetCountEval() {
        countEval = 0;
    }

    public void selection() {
        App.getDB().getCommunicationKeyDao().magicalDelete();
        App.getDB().getCommunicationDao().magicalDelete();
    }

    public static class StringProcessor {
        private StringProcessor() {
            throw new IllegalStateException("Это вспомогательный класс. Создание экземпляра не требуется.");
        }

        static String stringDeleteChars(String str, String chars) {
            return str.replaceAll(chars, "");
        }

        static List<KeyWord> getKeyWords(String string) {
            ArrayList<KeyWord> list = new ArrayList<>();
            for (KeyWord word : App.getDB().getKeyWordDao().getAll())
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
}

package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;
import root.ivatio.bd.Command.Command;
import root.ivatio.bd.CommunicationAPI;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.action.Action;
import root.ivatio.util.StorageAPI;
import root.ivatio.util.StringProcessor;

public class Ivor extends User {
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
    private static StorageAPI storageAPI = App.getStorageAPI();

    public Ivor(Resources resources, Action ... actions) {
        this.id = Long.valueOf(-1);
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
        return "Ivor";
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
        List<Question> questions = storageAPI.getQuestions();
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
        List<Command> commands = storageAPI.getCommands();
        for (Command cmd : commands) {
            HashSet<String> cmdSet = new HashSet<>(Arrays.asList(cmd.cmd.split(" ")));
            if (set.containsAll(cmdSet))
                return cmd;
        }
        return null;
    }

    private String processingCommand(Command c) {
        processingKeyWord = processingQuestion = false;
        for (Action action : actions)
            if (action.getCmd().equals(c.cmd)) {
                curAction = action;
                return action.next();
            }

        return resources.getString(R.string.noAction);
    }

    private String processingQuestion(Question q) {
        processingKeyWord = false;
        List<Answer> answers = storageAPI.getAnswerForQuestion(q.id);
        if (!answers.isEmpty()) {
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(q).memory(storageAPI.getCommunication(q.id, answer.id));
            processingQuestion = true;
            return answer.content;
        }
        processingQuestion = false;
        return resources.getString(R.string.ivorNoAnswer);
    }

    private String processingKeyWords(List<KeyWord> keyWords) {
        processingQuestion = false;
        for (KeyWord word : keyWords) {
            List<Answer> answers = storageAPI.getAnswerForKeyWord(word.id);
            if (answers.isEmpty())
                continue;
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(word).memory(storageAPI.getCommunicationKey(word.id, answer.id));
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
        CommunicationKey communicationKey = storageAPI.getCommunicationKey(keyWord.id, answer.id);
        if (communicationKey != null) {
            countEval++;
            communicationKey.power++;
            if (eval > 0)
                communicationKey.correct++;
            if (eval < 0)
                communicationKey.correct--;
            storageAPI.updateCommunicationKey(communicationKey);
        }
    }
    void reEvalutionQuestion(int eval) {
        Answer answer = getLastAnswer();
        Question question = getLastQuestion();
        Communication communication = storageAPI.getCommunication(question.id, answer.id);
        if (communication != null) {
            countEval++;
            communication.power++;
            if (eval > 0)
                communication.correct++;
            if (eval < 0)
                communication.correct--;
            storageAPI.updateCommunication(communication);
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
        long[] allID = storageAPI.getKeyWordsID();
        long anyID = allID[random.nextInt(allID.length)];
        return storageAPI.getKeyWord(anyID);
    }

    Message sendRandomQuestion() {
        Question r = getRandomQuestion();
        memoryQuestions.add(r);
        return new Message(null, r.content);
    }
    Question getRandomQuestion() {
        long[] allID = storageAPI.getQuestionsID();
        long anyID = allID[random.nextInt(allID.length)];
        return storageAPI.getQuestion(anyID);
    }

    boolean processingKeyWord() {
        return processingKeyWord;
    }
    boolean processingQuestion() {
        return processingQuestion;
    }
    void deleteLastKeyWord() {
        storageAPI.deleteKeyWord(getLastKeyWord());
    }
    void deleteLastQuestion() {
        storageAPI.deleteQuestion(getLastQuestion());
    }

    /** Удаление не только Communication, но и CommunicationKey, если она была последней*/
    void deleteLastCommunication() {
        CommunicationAPI lastCom = getLastCommunication();
        if(lastCom.getType() == CommunicationAPI.COMMUNICATION)
            storageAPI.deleteCommunication(lastCom.getID());
        else
            storageAPI.deleteCommunicationKey(lastCom.getID());
    }

    void appendNewAnswerForLastKW(Answer answer) {
        long answerID = storageAPI.insertAnswer(answer);
        KeyWord lastKeyWord = getLastKeyWord();
        if (lastKeyWord == null)
            return;
        CommunicationKey comKey = new CommunicationKey(lastKeyWord.id, answerID);
        storageAPI.insertCommunicationKey(comKey);
    }
    void appendNewAnswerForLastQ(Answer answer) {
        long answerID = storageAPI.insertAnswer(answer);
        Question lastQuestion = getLastQuestion();
        if (lastQuestion == null)
            return;
        Communication communication = new Communication(lastQuestion.id, answerID);
        storageAPI.insertCommunication(communication);
    }
    boolean appendNewKeyWord(KeyWord keyWord) {
        List<KeyWord> keyWords = storageAPI.getKeyWords();
        if (!keyWords.contains(keyWord)) {
            storageAPI.insertKeyWord(keyWord);
            return  true;
        }
        return false;
    }
    boolean appendNewQuestion(Question question) {
        List<Question> questions = storageAPI.getQuestions();
        if (!questions.contains(question)) {
            storageAPI.insertQuestion(question);
            return true;
        }
        return false;
    }

    public void resetCountEval() {
        countEval = 0;
    }

    public void selection() {
        storageAPI.selectionCommunication();
        storageAPI.selectionCommunicationKey();
    }
}

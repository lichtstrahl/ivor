package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;
import root.ivatio.bd.CommunicationAPI;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.action.Action;
import root.ivatio.network.NetworkObserver;
import root.ivatio.util.LocalStorageAPI;
import root.ivatio.util.StringProcessor;

public class Ivor extends User {
    private static final String name = "Ivor";
    public static final int criticalCountEval = 50;
    private Resources resources;
    private List<KeyWord> memoryWords;
    private List<Question> memoryQuestions;
    private List<Answer>   memoryAnswers;
    private List<CommunicationAPI> memoryCommunicationAPIS;
    private List<Action> actions;
    private int countEval;
    private Random random;
    private boolean processingKeyWord;
    private boolean processingQuestion;
    private Action curAction;
    private static LocalStorageAPI storageAPI = App.getStorageAPI();
    // Добавление новых данных
    private List<Question> newQuestions;
    private List<Answer> newAnswers;
    private List<KeyWord> newKeyWords;
    private List<Communication> newCommunications;
    private List<CommunicationKey> newCommunicationKeys;
    private NetworkObserver<Question> insertQuestionObserver;
    private NetworkObserver<Answer> insertAnswerObserver;
    private NetworkObserver<KeyWord> insertKeyWordObserver;
    private NetworkObserver<Communication> insertCommunicationObserver;
    private NetworkObserver<CommunicationKey> insertCommunicationKeyObserver;
    private NetworkObserver<Communication> replaceCommunicationObserver;
    private NetworkObserver<CommunicationKey> replaceCommunicationKeyObserver;
    // Списки для отображения ID с местных значений в серверные
    private List<HolderID> questionHolderID;
    private List<HolderID> keyWordHolderID;
    private List<HolderID> communicationHolderID;
    private List<HolderID> communicationKeyHolderID;


    public Ivor(Resources resources, Action ... actions) {
        this.id = Long.valueOf(-1);
        this.resources = resources;
        this.memoryWords = new LinkedList<>();
        this.memoryQuestions = new LinkedList<>();
        this.memoryAnswers = new LinkedList<>();
        this.memoryCommunicationAPIS = new LinkedList<>();
        this.random = new Random();
        this.processingKeyWord = false;
        this.countEval = 0;
        this.actions = Arrays.asList(actions);
        this.curAction = null;
        this.newQuestions = new LinkedList<>();
        this.newAnswers = new LinkedList<>();
        this.newKeyWords = new LinkedList<>();
        this.newCommunications = new LinkedList<>();
        this.newCommunicationKeys = new LinkedList<>();
        this.insertQuestionObserver = new NetworkObserver<>(this::successfulInsertQuestion, this::errorNetwork);
        this.insertAnswerObserver = new NetworkObserver<>(this::successfulInsertAnswer, this::errorNetwork);
        this.insertKeyWordObserver = new NetworkObserver<>(this::successfulInsertKeyWord, this::errorNetwork);
        this.insertCommunicationObserver = new NetworkObserver<>(this::successfulInsertCommunication, this::errorNetwork);
        this.insertCommunicationKeyObserver = new NetworkObserver<>(this::successfulInsertCommunicationKey, this::errorNetwork);
        this.questionHolderID = new LinkedList<>();
        this.keyWordHolderID = new LinkedList<>();
        this.communicationHolderID = new LinkedList<>();
        this.communicationKeyHolderID = new LinkedList<>();
        this.replaceCommunicationObserver = new NetworkObserver<>(this::successfulReplaceCommunication, this::errorNetwork);
        this.replaceCommunicationKeyObserver = new NetworkObserver<>(this::successfulReplaceCommunicationKey, this::errorNetwork);
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


    int getCountEval() {
        return countEval;
    }

    List<String> resetAction() {
        List<String> res = curAction.getParam();
        curAction = null;
        return res;
    }

    void reEvalutionKeyWord(int eval) {
        Answer answer = getLastAnswer();
        KeyWord keyWord = getLastKeyWord();
        if (keyWord == null || answer == null)
            return;
        CommunicationKey communicationKey = storageAPI.getCommunicationKey(keyWord.id, answer.id);
        if (communicationKey != null) {
            countEval++;
            communicationKey.power++;
            if (eval > 0)
                communicationKey.correct++;
            if (eval < 0)
                communicationKey.correct--;
            storageAPI.updateCommunicationKey(communicationKey);

            if (newCommunicationKeys.contains(communicationKey)) {
                for (HolderID holder : communicationKeyHolderID) {
                    if (communicationKey.id == holder.selfID) {
                        communicationKey.id = holder.serverID;
                        break;
                    }
                }
            }
            App.getLoadAPI().replaceCommunicationKey(communicationKey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(replaceCommunicationKeyObserver);
        }
    }
    void reEvalutionQuestion(int eval) {
        Answer answer = getLastAnswer();
        Question question = getLastQuestion();
        if (question == null || answer ==null)
            return;
        Communication communication = storageAPI.getCommunication(question.id, answer.id);
        if (communication != null) {
            countEval++;
            communication.power++;
            if (eval > 0)
                communication.correct++;
            if (eval < 0)
                communication.correct--;
            storageAPI.updateCommunication(communication);
            // Теперь нужно обновить коммуникацию на сервере. Для этого необходимо узнать серверный ID для данной коммуникации
            if (newCommunications.contains(communication)) {    // Если новая, значит нужно "подменить" ID
                for (HolderID holder : communicationHolderID) {
                    if (holder.selfID == communication.id) {
                        communication.id = holder.serverID;
                        break;
                    }
                }
            }

            // Обновляем Communication на сервере с действительным ID
            App.getLoadAPI().replaceCommunication(communication)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(replaceCommunicationObserver);
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
        if (lastCom == null)
            return;
        if(lastCom.getType() == CommunicationAPI.COMMUNICATION)
            storageAPI.deleteCommunication(lastCom.getID());
        else
            storageAPI.deleteCommunicationKey(lastCom.getID());
    }

    void appendNewAnswerForLastKW(Answer answer) {
        answer.id = storageAPI.insertAnswer(answer);
        newAnswers.add(answer);
        App.getLoadAPI().insertAnswer(answer.toDTO())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(insertAnswerObserver);

        KeyWord lastKeyWord = getLastKeyWord();
        if (lastKeyWord == null)
            return;
        CommunicationKey comKey = new CommunicationKey(lastKeyWord.id, answer.id);
        comKey.id = storageAPI.insertCommunicationKey(comKey);
        newCommunicationKeys.add(comKey);
    }
    void appendNewAnswerForLastQ(Answer answer) {
        answer.id = storageAPI.insertAnswer(answer);
        newAnswers.add(answer);
        App.getLoadAPI().insertAnswer(answer.toDTO())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(insertAnswerObserver);

        Question lastQuestion = getLastQuestion();
        if (lastQuestion == null)
            return;
        Communication communication = new Communication(lastQuestion.id, answer.id);
        communication.id = storageAPI.insertCommunication(communication);
        newCommunications.add(communication);
    }
    boolean appendNewKeyWord(KeyWord keyWord) {
        List<KeyWord> keyWords = storageAPI.getKeyWords();
        if (!keyWords.contains(keyWord)) {
            keyWord.id = storageAPI.insertKeyWord(keyWord);
            newKeyWords.add(keyWord);
            App.getLoadAPI().insertKeyWord(keyWord.toDTO())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(insertKeyWordObserver);
            return  true;
        }
        return false;
    }
    boolean appendNewQuestion(Question question) {
        List<Question> questions = storageAPI.getQuestions();
        if (!questions.contains(question)) {
            question.id = storageAPI.insertQuestion(question);
            newQuestions.add(question);
            App.getLoadAPI().insertQuestion(question.toDTO())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(insertQuestionObserver);
            return true;
        }
        return false;
    }

    void resetCountEval() {
        countEval = 0;
    }

    public void selection() {
        storageAPI.selectionCommunication();
        storageAPI.selectionCommunicationKey();
    }

    List<Question> getNewQuestions() {
        return newQuestions;
    }

    List<Answer> getNewAnswers() {
        return newAnswers;
    }

    List<KeyWord> getNewKeyWords() {
        return newKeyWords;
    }

    List<Communication> getNewCommunications() {
        return newCommunications;
    }

    List<CommunicationKey> getNewCommunicationKeys() {
        return newCommunicationKeys;
    }

    void insertCommunication() {
        // Метод не используется
    }

    public void unsubscribe() {
        insertQuestionObserver.unsubscribe();
        insertAnswerObserver.unsubscribe();
        insertKeyWordObserver.unsubscribe();
        insertCommunicationObserver.unsubscribe();
        insertCommunicationKeyObserver.unsubscribe();
    }

    private void successfulInsertQuestion(Question question) {
        long serverID = question.id;
        long oldID = -1;
        for (Question q : newQuestions)
            if (q.content.equals(question.content)) {
                oldID = q.id;
                q.id = serverID;
        }
        questionHolderID.add(new HolderID(oldID, serverID));
        App.logI(String.format(Locale.ENGLISH, "%s : type %s : oldID = %d : serverID = %d",
                resources.getString(R.string.successfulPost), "Question", oldID, serverID));
    }

    // Если был добавлен Answer, значит вместе с ним нужно добавить либо один Communication, либо один CommunicationKey
    private void successfulInsertAnswer(Answer answer) {
        long serverID = answer.id;
        long oldID = -1;
        for (Answer a : newAnswers)
            if (a.content.equals(answer.content)) {
                oldID = a.id;
                a.id = serverID;
            }

        App.logI(String.format(Locale.ENGLISH, "%s : type %s : oldID = %d : serverID = %d",
                resources.getString(R.string.successfulPost), "Answer", oldID, serverID));

        // Ищем (по старому id) какие коммуникации соответствуют нашему ответу. Она будет одна (либо Com, либо ComKey)
        List<Communication> coms = App.getStorageAPI().getCommunicationsForAnswer(oldID);
        List<CommunicationKey> comKeys = App.getStorageAPI().getCommunicationKeysForAnswer(oldID);


        if (!coms.isEmpty()) { // Если нужно добавить Communication, но сначала изменить answerID и questionID
            Communication c = coms.get(0);
            c.answerID = serverID;
            // Ищем, в какой серверный ID ревращается местный ID нужного нам вопроса
            for (HolderID holder : questionHolderID) {
                if (holder.selfID == c.questionID) {
                    c.questionID = holder.serverID;
                    App.logI(String.format(Locale.ENGLISH, "Успешная трансформация для Communication qID: %d", c.questionID));
                    break;
                }
            }
            // Отправляем обновленный Communication на сервер
            App.getLoadAPI().insertCommunication(c.toDTO())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(insertCommunicationObserver);
        } else if (!comKeys.isEmpty()) { // Если нужно добавить CommunicationKey, но сначала изменить answerID и keyID
            CommunicationKey c = comKeys.get(0);
            c.answerID = serverID;
            // Ищем, в какой серверный ID превращаетс местный ID нужного нам ключевого слова
            for (HolderID holder : keyWordHolderID) {
                if (holder.selfID == c.keyID) {
                    c.keyID = holder.serverID;
                    App.logI(String.format(Locale.ENGLISH, "Успешная трансформация для CommunicationKey keyID: %d", c.keyID));
                    break;
                }
            }
            // Отправляем обновленный CommunicationKey на сервер
            App.getLoadAPI().insertCommunicationKey(c.toDTO())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(insertCommunicationKeyObserver);
        } else {    // Вообще не удалось найти таких коммуникаций
            App.logW("Не удалось найти коммуникаций, соответствующих данному ответу.");
        }
    }

    private void successfulInsertKeyWord(KeyWord word) {
        long serverID = word.id;
        long oldID = -1;
        for (KeyWord w : newKeyWords) {
            if (w.content.equals(word.content)) {
                oldID = w.id;
                w.id = serverID;
            }
        }
        keyWordHolderID.add(new HolderID(oldID, serverID));

        App.logI(String.format(Locale.ENGLISH, "%s : type %s : oldID = %d : serverID = %d",
                resources.getString(R.string.successfulPost), "KeyWord", oldID, serverID));
    }

    private void successfulInsertCommunication(Communication communication) {
        long serverID = communication.id;
        long oldID = -1;
        for (Communication c : newCommunications) {
            if (c.equals(communication)) {
                oldID = c.id;
                c.id = serverID;
            }
        }
        communicationHolderID.add(new HolderID(oldID, serverID));

        App.logI(resources.getString(R.string.successfulPost) + ": Communication : id = " + communication.id);
    }

    private void successfulInsertCommunicationKey(CommunicationKey communicationKey) {
        long serverID = communicationKey.id;
        long oldID = -1;
        for (CommunicationKey comKey : newCommunicationKeys) {
            if (comKey.equals(communicationKey)) {
                oldID = comKey.id;
                comKey.id = serverID;
            }
        }
        communicationKeyHolderID.add(new HolderID(oldID, serverID));

        App.logI(resources.getString(R.string.successfulPost) + ": CommunicationKey : id = " + communicationKey.id);

    }

    private void successfulReplaceCommunicationKey(CommunicationKey communicationKey) {
        App.logI(resources.getString(R.string.successfulUpdate));
    }

    private void successfulReplaceCommunication(Communication communication) {
        App.logI(resources.getString(R.string.successfulUpdate));
    }

    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }


    class HolderID {
        private long selfID;
        private long serverID;

        HolderID(long i0, long i1) {
            selfID = i0;
            serverID = i1;
        }
    }
}

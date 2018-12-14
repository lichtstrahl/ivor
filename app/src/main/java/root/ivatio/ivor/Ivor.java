package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.App;
import root.ivatio.Message;
import root.ivatio.R;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.action.Action;
import root.ivatio.network.NetworkObserver;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.util.HolderID;
import root.ivatio.util.LocalStorageAPI;
import root.ivatio.util.ServerIDAdapter;
import root.ivatio.util.StringProcessor;

public class Ivor extends User {
    private static final String NAME = "Ivor";
    static final int CRITICAL_COUNT_EVAL = 50;
    private Resources resources;
    private List<KeyWord> memoryWords;
    private List<Question> memoryQuestions;
    private List<Answer>   memoryAnswers;
    private List<Object> memoryCommunication;
    private List<Action> actions;
    private int countEval;
    private Random random;
    private boolean processingKeyWord;
    private boolean processingQuestion;
    private Action curAction;
    private static LocalStorageAPI storageAPI;
    // Добавление новых данных
    private List<Question> newQuestions;
    private List<Answer> newAnswers;
    private List<KeyWord> newKeyWords;
    private List<Communication> newCommunications;
    private List<CommunicationKey> newCommunicationKeys;
    // Наблюдатели за сетевыми запросами
    private NetworkObserver<Question> insertQuestionObserver;
    private NetworkObserver<Answer> insertAnswerObserver;
    private NetworkObserver<KeyWord> insertKeyWordObserver;
    private NetworkObserver<Communication> insertCommunicationObserver;
    private NetworkObserver<CommunicationKey> insertCommunicationKeyObserver;
    private NetworkObserver<Communication> replaceCommunicationObserver;
    private NetworkObserver<CommunicationKey> replaceCommunicationKeyObserver;
    private NetworkObserver<EmptyDTO> deleteKeyWordObserver;
    private NetworkObserver<EmptyDTO> deleteQuestionObserver;
    private NetworkObserver<EmptyDTO> deleteCommunicationObserver;
    private NetworkObserver<EmptyDTO> deleteCommunicationKeyObserver;
    private NetworkObserver<EmptyDTO> selectionObserver;
    // Списки для отображения ID с местных значений в серверные
    private List<HolderID> questionHolderID;
    private List<HolderID> keyWordHolderID;
    private List<HolderID> communicationHolderID;
    private List<HolderID> communicationKeyHolderID;

    public Ivor(Resources resources, LocalStorageAPI api, Action ... actions) {
        this.id = Long.valueOf(-1);
        this.resources = resources;
        this.memoryWords = new LinkedList<>();
        this.memoryQuestions = new LinkedList<>();
        this.memoryAnswers = new LinkedList<>();
        this.memoryCommunication = new LinkedList<>();
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
        this.deleteKeyWordObserver = new NetworkObserver<>(this::successfulNetwork, this::errorNetwork);
        this.deleteQuestionObserver = new NetworkObserver<>(this::successfulNetwork, this::errorNetwork);
        this.deleteCommunicationObserver = new NetworkObserver<>(this::successfulNetwork, this::errorNetwork);
        this.deleteCommunicationKeyObserver = new NetworkObserver<>(this::successfulNetwork, this::errorNetwork);
        this.selectionObserver = new NetworkObserver<>(this::successfulNetwork, this::errorNetwork);
        this.storageAPI = api;
    }

    public static String getName() {
        return NAME;
    }

    private Observable<String> rxProcessingRequest(String liteRequest) {

//        return App.getLoadAPI().loadCommands()
//                .flatMap((commands) -> {    // КРазбор команд
//                    Command cmd = isCommand(liteRequest, commands);
//                    if (cmd != null)
//                        return Observable.just(cmd);
//                    else
//                        return Observable.just(Integer.valueOf(0));
//                }).flatMap((args) -> {    // args: Command, 0
//                    if (args.equals(0)) {
//                        return App.getLoadAPI().loadQuestions();
//                    } else {
//                        processingQuestion = processingKeyWord = false;
//                        return Observable.just((Command)args);
//                    }
//                })
//                .flatMap((args) -> {    // args: List<Question>, Command
//                    if (args instanceof Command) return Observable.just((Command)args);
//                    List<Question> list = (List<Question>)args;
//                    Question q = isQuestion(liteRequest, list);
//                    if (q != null) {
//                        return Observable.just(q);
//                    } else {
//                        return Observable.just(Integer.valueOf(0));
//                    }
//                })
//                .flatMap((arg) -> {     // args: Command, Question, 0
//                    if (arg instanceof Command) return Observable.just((Command)arg);
//                    if (arg.equals(0)) {  // Значит вопрос не был найден
//                        return Observable.just(Integer.valueOf(0)); // Дополнительный проброс
//                    } else {            // Был найден вопрос
//                        return App.getLoadAPI().loadCommunicationsForQuestion(((Question)arg).id);
//                    }
//                })
//                .flatMap((arg) -> {     // args: Command, 0, List<Communication>
//                    if (arg instanceof Command) return Observable.just((Command)arg);
//                    List<Communication> communications = (List)arg;
//                    if (!communications.isEmpty()) {
//                        int r = random.nextInt(communications.size());
//                        Communication communication = communications.get(r);
//                        return Observable.just(communication);
//                    }
//                    else {
//                        return App.getLoadAPI().loadKeyWords();
//                    }
//                })
//                .flatMap(arg -> {   // Command, Communication, List<KeyWord>
//                    if (arg instanceof Command) return Observable.just((Command)arg);
//                    if (arg instanceof Communication) { // Найдена коммуникация
//                        Communication com = (Communication)arg;
//                        processingQuestion = true;
//                        processingKeyWord = false;
//                        memory(com);
//                        return Observable.combineLatest(
//                                App.getLoadAPI().loadAnswerByID(com.answerID),
//                                App.getLoadAPI().loadQuestionByID(com.questionID),
//                                (Answer a, Question q) -> {
//                                    memory(q).memory(a);
//                                    return a;
//                                }
//                        );
//                    }
//                    List<KeyWord> words = (List)arg;
//                    List<KeyWord> words2 = StringProcessor.getKeyWords(liteRequest);
//                })
//                .flatMap(arg -> {   // Command, Answer, 0
//
//                });
        Observable<String> obsCommand = App.getLoadAPI().loadCommands()
                .flatMap(commands -> {
                   Command cmd = isCommand(liteRequest, commands);
                   if (cmd != null)
                       return Observable.just(processingCommand(cmd));
                   else
                       return Observable.just("");
                });

        Observable<String> obsQuestion = App.getLoadAPI().loadQuestions()
                .flatMap(questions -> {
                    Question q = isQuestion(liteRequest, questions);
                    if (q != null)
                        return App.getLoadAPI().loadCommunicationsForQuestion(q.id);
                    else
                        return Observable.just("");
                })
                .flatMap(arg -> {   // List<Communication>, ""
                    if (arg instanceof List) {
                        List<Communication> communications = (List)arg;
                        if (!communications.isEmpty()) {
                            int r = random.nextInt(communications.size());
                            Communication com = communications.get(r);
                            memory(com);

                            return Observable.combineLatest(
                                    App.getLoadAPI().loadAnswerByID(com.answerID),
                                    App.getLoadAPI().loadQuestionByID(com.questionID),
                                    (Answer a, Question q) -> {
                                        memory(a).memory(q);
                                        return a.content;
                                    });
                        }
                    }
                    return Observable.just("");
                });

        Observable<String> obsKeyWord = App.getLoadAPI().loadKeyWords()
                .flatMap(allWords -> {
                    List<KeyWord> words = StringProcessor.getKeyWords(liteRequest, allWords);
                    if (!words.isEmpty()) {
                        int r = random.nextInt(words.size());
                        KeyWord word = words.get(r);
                        return App.getLoadAPI().loadCommunicationKeysForKeyWord(word.id);
                    }
                    return Observable.just("");
                })
                .flatMap(arg -> { // List<CommunicationKey>, ""
                    if (arg instanceof List) {
                        List<CommunicationKey> keys = (List)arg;
                        if (!keys.isEmpty()) {
                            int r = random.nextInt(keys.size());
                            CommunicationKey communicationKey = keys.get(r);
                            memory(communicationKey);

                            return Observable.combineLatest(
                                    App.getLoadAPI().loadAnswerByID(communicationKey.answerID),
                                    App.getLoadAPI().loadKeyWordByID(communicationKey.keyID),
                                    (Answer answer, KeyWord key) -> {
                                        memory(answer).memory(key);
                                        return answer.content;
                                    });
                        }
                    }
                    return Observable.just("");
                });

        return Observable.combineLatest(obsCommand, obsQuestion, obsKeyWord,
                (strCommand, strQuestion, strWord) -> {
                    processingKeyWord = false;
                    processingQuestion = false;
                    if (!strCommand.isEmpty()) {
                       return strCommand;
                    }
                    if (!strQuestion.isEmpty()) {
                        processingQuestion = true;
                        return strQuestion;
                    }
                    if (!strWord.isEmpty()) {
                        processingKeyWord = true;
                       return strWord;
                    }
                    return resources.getString(R.string.ivorNoAnswer);
                });
    }

    @Nullable
    private Question isQuestion(String str, List<Question> questions) {
        String []words = str.split(" ");
        HashSet<String> set = new HashSet<>(Arrays.asList(words));
        // Обработка Question
        for (Question q : questions) {
            String[] qwords = q.content.split(" ");
            HashSet<String> qSet = new HashSet<>(Arrays.asList(qwords));
            if (qSet.equals(set))
                return q;
        }
        return null;
    }

    @Nullable
    private Command isCommand(String str, List<Command> commands) {
        HashSet<String> set = new HashSet<>(Arrays.asList(str.split(" ")));
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
    private Ivor memory(Object api) {
        memoryCommunication.add(api);
        return this;
    }

    Observable<String> rxAnswer(String request) {
        String liteString = StringProcessor.toStdFormat(request);
        if (curAction == null) {
            return rxProcessingRequest(liteString);
        } else {
            curAction.put(liteString);
            return Observable.just(curAction.next());
        }
    }

    private Message send(String content) {
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
                ServerIDAdapter.adapterID(communicationKey, communicationKeyHolderID);
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
                ServerIDAdapter.adapterID(communication, communicationHolderID);
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

    private Object getLastCommunication() {
        if (memoryCommunication.isEmpty())
            return null;
        return memoryCommunication.get(memoryCommunication.size()-1);
    }

    @NonNull
    Message sendRandomKeyWord() {
        KeyWord r = getRandomKeyWord();
        if (r == null)
            return sendEmptyMessage();
        memoryWords.add(r);
        return new Message(null, r.content);
    }

    private Message sendEmptyMessage() {
        return new Message(null, "");
    }

    @Nullable
    private KeyWord getRandomKeyWord() {
        long[] allID = storageAPI.getKeyWordsID();
        if (allID.length == 0)
            return null;
        long anyID = allID[random.nextInt(allID.length)];
        return storageAPI.getKeyWord(anyID);
    }

    @NonNull
    Message sendRandomQuestion() {
        Question r = getRandomQuestion();
        if (r == null)
            return sendEmptyMessage();
        memoryQuestions.add(r);
        return new Message(null, r.content);
    }

    @Nullable
    private Question getRandomQuestion() {
        long[] allID = storageAPI.getQuestionsID();
        if (allID.length == 0)
            return null;
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
        KeyWord word = getLastKeyWord();
        if (word == null)
            return;
        storageAPI.deleteKeyWord(word);

        if (newKeyWords.contains(word)) {
            ServerIDAdapter.adapterID(word, keyWordHolderID);
        }
        App.getLoadAPI().deleteKeyWord(word.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteKeyWordObserver);
    }
    void deleteLastQuestion() {
        Question question = getLastQuestion();
        if (question == null)
            return;
        storageAPI.deleteQuestion(question);

        if (newQuestions.contains(question)) {
            ServerIDAdapter.adapterID(question, questionHolderID);
        }
        App.getLoadAPI().deleteQuestion(question.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteQuestionObserver);
    }

    /** Удаление не только Communication, но и CommunicationKey, если она была последней*/
    void deleteLastCommunication() {
        Object o = getLastCommunication();
        if (o == null)
            return;
        if(o instanceof Communication) {
            Communication c = (Communication)o;
            storageAPI.deleteCommunication(c.id);

            if (newCommunications.contains(c)) {
                ServerIDAdapter.adapterID(c, communicationHolderID);
            }
            App.getLoadAPI().deleteCommunication(c.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(deleteCommunicationObserver);
        } else {
            CommunicationKey c = (CommunicationKey)o;
            storageAPI.deleteCommunicationKey(c.id);

            if (newCommunicationKeys.contains(c)) {
                ServerIDAdapter.adapterID(c, communicationKeyHolderID);
            }

            App.getLoadAPI().deleteCommunicationKey(c.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(deleteCommunicationKeyObserver);
        }
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
        App.getLoadAPI().selection()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(selectionObserver);
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
        deleteCommunicationKeyObserver.unsubscribe();
        deleteCommunicationObserver.unsubscribe();
        deleteKeyWordObserver.unsubscribe();
        deleteQuestionObserver.unsubscribe();
        replaceCommunicationKeyObserver.unsubscribe();
        replaceCommunicationObserver.unsubscribe();
        selectionObserver.unsubscribe();
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
        List<Communication> coms = storageAPI.getCommunicationsForAnswer(oldID);
        List<CommunicationKey> comKeys = storageAPI.getCommunicationKeysForAnswer(oldID);


        if (!coms.isEmpty()) { // Если нужно добавить Communication, но сначала изменить answerID и questionID
            Communication c = coms.get(0);
            c.answerID = serverID;
            // Ищем, в какой серверный ID ревращается местный ID нужного нам вопроса
            ServerIDAdapter.adapterQuestionID(c, questionHolderID);
            // Отправляем обновленный Communication на сервер
            App.getLoadAPI().insertCommunication(c.toDTO())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(insertCommunicationObserver);
        } else if (!comKeys.isEmpty()) { // Если нужно добавить CommunicationKey, но сначала изменить answerID и keyID
            CommunicationKey c = comKeys.get(0);
            c.answerID = serverID;
            // Ищем, в какой серверный ID превращаетс местный ID нужного нам ключевого слова
            ServerIDAdapter.adapterKeyID(c, keyWordHolderID);
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

    private void successfulNetwork(Object o) {
        App.logI(resources.getString(R.string.successfulPost));
    }

    private void errorNetwork(Throwable t) {
        App.logE(t.getMessage());
    }
}

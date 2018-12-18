package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;

import java.security.Key;
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
    }

    public static String getName() {
        return NAME;
    }

    private Observable<String> processingRequest(String liteRequest) {
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
    private Ivor memory(Object com) {
        memoryCommunication.add(com);
        return this;
    }

    Observable<String> rxAnswer(String request) {
        String liteString = StringProcessor.toStdFormat(request);
        if (curAction == null) {
            return processingRequest(liteString);
        } else {
            curAction.put(liteString);
            return Observable.just(curAction.next());
        }
    }

    public Message send(String content) {
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

    Observable<String> evaluation(int eval) {
        Observable<CommunicationKey> obsKey = null;
        if (processingKeyWord) {
            CommunicationKey com = (CommunicationKey)getLastCommunication();
            if (com != null) {
                countEval++;
                com.power++;
                if (eval > 0)
                    com.correct++;
                if (eval < 0)
                    com.correct--;


                obsKey = App.getLoadAPI().replaceCommunicationKey(com);
            }
        }

        Observable<Communication> obsCom = null;
        if (processingQuestion) {
            Communication com = (Communication)getLastCommunication();
            if (com != null) {
                countEval++;
                com.power++;
                if (eval > 0)
                    com.correct++;
                if (eval < 0)
                    com.correct--;

                // Обновляем Communication на сервере с действительным ID
                obsCom = App.getLoadAPI().replaceCommunication(com);
            }
        }

        if (obsCom != null) {
            return obsCom.flatMap(com -> Observable.just("com"));
        }

        if (obsKey != null) {
            return obsKey.flatMap(key -> Observable.just("key"));
        }

        return Observable.just("");
    }


    @Nullable
    private KeyWord getLastKeyWord() {
        if (memoryWords.isEmpty())
            return null;
        return memoryWords.get(memoryWords.size()-1);
    }

    @Nullable
    private Question getLastQuestion() {
        if (memoryQuestions.isEmpty())
            return null;
        return memoryQuestions.get(memoryQuestions.size()-1);
    }

    @Nullable
    private Answer getLastAnswer() {
        if (memoryAnswers.isEmpty())
            return null;
        return memoryAnswers.get(memoryAnswers.size()-1);
    }

    @Nullable
    private Object getLastCommunication() {
        if (memoryCommunication.isEmpty())
            return null;
        return memoryCommunication.get(memoryCommunication.size()-1);
    }

    public Observable<Message> sendRandomKeyWord() {
        return App.getLoadAPI().loadKeyWords()
                .flatMap(words -> {
                    String content = "";
                    if (!words.isEmpty()) {
                        int r = random.nextInt(words.size());
                        KeyWord word = words.get(r);
                        content = word.content;
                        memory(word);
                    }
                    return Observable.just(new Message(null, content));
                });
    }

    public Observable<Message> sendRandomQuestion() {
        return App.getLoadAPI().loadQuestions()
                .flatMap(questions -> {
                   String content = "";
                   if (questions.isEmpty()) {
                        int r = random.nextInt(questions.size());
                        Question q = questions.get(r);
                        content = q.content;
                        memory(q);
                   }
                   return Observable.just(new Message(null, content));
                });
    }

    private Message sendEmptyMessage() {
        return new Message(null, "");
    }

    boolean processingKeyWord() {
        return processingKeyWord;
    }

    boolean processingQuestion() {
        return processingQuestion;
    }


    Observable<EmptyDTO> rxDeleteLastCommunication() {
        Object o = getLastCommunication();
        if (o instanceof Communication) {
            Communication c = (Communication)o;

            return App.getLoadAPI().deleteCommunication(c.id);
        }
        if (o instanceof CommunicationKey) {
            CommunicationKey key = (CommunicationKey)o;
            return App.getLoadAPI().deleteCommunicationKey(key.id);
        }
        return Observable.just(new EmptyDTO());
    }



    Observable<Message> rxDeleteQuestion() {
        Question q = getLastQuestion();
        return App.getLoadAPI().deleteQuestion(q.id)
                .flatMap(dto -> sendRandomQuestion());
    }

    Observable<Message> rxDeleteKeyWord() {
        KeyWord word = getLastKeyWord();
        return App.getLoadAPI().deleteKeyWord(word.id)
                .flatMap(dto -> sendRandomKeyWord());
    }

    Observable<CommunicationKey> rxAppendNewAnswerForLastWK(Answer answer) {
        return App.getLoadAPI().insertAnswer(answer.toDTO())
                .flatMap(a -> Observable.just(a.id))
                .flatMap(id -> {
                   CommunicationKey com = new CommunicationKey(getLastKeyWord().id, id);
                   return App.getLoadAPI().insertCommunicationKey(com.toDTO());
                });
    }

    Observable<Boolean> rxAppendNewKW(KeyWord word) {
        return App.getLoadAPI().loadKeyWords()
                .flatMap(words -> Observable.just(words.contains(word)))
                .flatMap(flag -> { // true, false
                    if (!flag)
                        return App.getLoadAPI().insertKeyWord(word.toDTO());
                    return Observable.just(flag);
                })
                .flatMap(arg -> Observable.just(arg instanceof KeyWord));
    }

    Observable<Boolean> rxAppendNewQ(Question q) {
        return App.getLoadAPI().loadQuestions()
                .flatMap(questions -> Observable.just(questions.contains(q)))
                .flatMap(flag -> {
                    if (!flag)
                        return App.getLoadAPI().insertKeyWord(q.toDTO());
                    return Observable.just(flag);
                })
                .flatMap(arg -> Observable.just(arg instanceof Question));
    }

    Observable<Communication> rxAppendNewAnswerForQuestion(Answer answer) {
        return App.getLoadAPI().insertAnswer(answer.toDTO())
                .flatMap(a -> Observable.just(a.id))
                .flatMap(id -> {
                    Communication c = new Communication(getLastQuestion().id, id);
                    return App.getLoadAPI().insertCommunication(c.toDTO());
                });
    }

    void resetCountEval() {
        countEval = 0;
    }

    void selection() {

    }
}

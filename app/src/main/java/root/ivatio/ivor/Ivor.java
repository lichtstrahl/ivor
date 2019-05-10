package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import android.content.res.Resources;
import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import root.ivatio.app.App;
import root.ivatio.activity.msg.Message;
import root.ivatio.R;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.command.Command;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;
import root.ivatio.ivor.action.Action;
import root.ivatio.network.observer.NetworkObserver;
import root.ivatio.network.dto.EmptyDTO;
import root.ivatio.bd.LocalStorageAPI;

public class Ivor extends User {
    private static final String name = "Ivor";
    public static final int criticalCountEval = 50;
    private Resources resources;
    private List<KeyWord> memoryWords;
    private List<Question> memoryQuestions;
    private List<Answer>   memoryAnswers;
    private List<Object> memoryCommunicationAPIS;
    private List<Action> actions;
    private int countEval;
    private Random random;
    private Action curAction;
    private static LocalStorageAPI storageAPI = App.getStorageAPI();

    public Ivor(Resources resources, Action ... actions) {
        this.id = Long.valueOf(-1);
        this.resources = resources;
        this.memoryWords = new LinkedList<>();
        this.memoryQuestions = new LinkedList<>();
        this.memoryAnswers = new LinkedList<>();
        this.memoryCommunicationAPIS = new LinkedList<>();
        this.random = new Random();
        this.countEval = 0;
        this.actions = Arrays.asList(actions);
        this.curAction = null;
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
        for (Action action : actions)
            if (action.getCmd().equals(c.cmd)) {
                curAction = action;
                return action.next();
            }

        return resources.getString(R.string.noAction);
    }

    private String processingQuestion(Question q) {
        List<Answer> answers = storageAPI.getAnswerForQuestion(q.id);
        if (!answers.isEmpty()) {
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(q).memory(storageAPI.getCommunication(q.id, answer.id));
            return answer.content;
        }
        return resources.getString(R.string.ivorNoAnswer);
    }

    private String processingKeyWords(List<KeyWord> keyWords) {
        for (KeyWord word : keyWords) {
            List<Answer> answers = storageAPI.getAnswerForKeyWord(word.id);
            if (answers.isEmpty())
                continue;
            int r = random.nextInt(answers.size());
            Answer answer = answers.get(r);
            memory(answer).memory(word).memory(storageAPI.getCommunicationKey(word.id, answer.id));
            return answer.content;
        }

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
    private Ivor memory(Object api) {
        memoryCommunicationAPIS.add(api);
        return this;
    }

    @Nullable
    public Message answer(String request) {
        return send(processingMessage(request));
    }

    public Message send(String content) {
        if (content.isEmpty())
            return null;
        return new Message(null, content);
    }

    public void saveQuestion(long id) {
        memory(Question.createQuestion("", id));
    }
}

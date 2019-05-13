package root.ivatio.ivor;

// Основной класс, отвечающий за AI
// Общение и так далее

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.users.User;

public class Ivor extends User {
    private static final String name = "Ivor";
    private List<Question> memoryQuestions;

    public Ivor() {
        this.id = Long.valueOf(-1);
        this.memoryQuestions = new LinkedList<>();
    }

    public static String getName() {
        return name;
    }

    private Ivor memory(Question q) {
        memoryQuestions.add(q);
        return this;
    }

    @Nullable
    public Question getLastQuestion() {
        if (memoryQuestions.isEmpty()) {
            return null;
        } else {
            return memoryQuestions.get(memoryQuestions.size()-1);
        }
    }

    public void saveQuestion(long id) {
        memory(Question.createQuestion("", id));
    }
}

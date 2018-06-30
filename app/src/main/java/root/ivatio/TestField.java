package root.ivatio;

import BD.Answer.Answer;
import BD.Communication.Communication;
import BD.Mood.Mood;
import BD.Qustion.Question;
import BD.Users.User;

import static root.ivatio.App.getDB;

// Класс, который заполняет БД тестовыми данными
public class TestField {
    public static void fillQuestion() {
        Question question[] = new Question[] {
                new Question("Как у тебя дела?"),
                new Question("Как прошёл день?"),
                new Question("Можно тебя попросить?")
        };

        for (Question q : question)
            getDB().getQuestionDao().insert(q);
    }

    public static void fillAnswer() {
        Answer answer[] = new Answer[] {
                new Answer("У меня всё хорошо, спасибо, что спросил!"),
                new Answer("Я очень люблю любоваться ночным небом."),
                new Answer("Я вот недавно узнала, что волк является прямым  предком обычной домашней собаки!")
        };

        for (Answer a : answer)
            getDB().getAnswerDao().insert(a);
    }

    public static void fillMood() {
        Mood mood[] = new Mood[] {
                new Mood(0, 50)
        };

        for (Mood m : mood)
            getDB().getMoodDao().insert(m);
    }

    public static void fillCommunication() {
         Communication communication[] = new Communication[] {
                 new Communication(0,0, 100)
         };

         for (Communication c : communication)
             getDB().getCommunicationDao().insert(c);
    }

    public static void fillUser() {
        User user = User.getUserBuilder()
                .buildName("Игорь")
                .buildLogin("rainbow")
                .buildPassword("i1g9o9r7")
                .buildAge(21)
                .buildCity("Ивантеевка")
                .buildEmail("cool.rainbow2012@yandex.ru")
                .buildTimeEntry()
                .build();
        getDB().getUserDao().insert(user);
    }
}

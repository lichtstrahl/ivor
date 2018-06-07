package root.ivatio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import Answer.Answer;
import Answer.AnswerDao;
import Qustion.Question;
import Qustion.QuestionDao;
import Users.User;
import Users.UserDao;

@Database(entities = {Question.class, Answer.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
//    public abstract
    public abstract QuestionDao getQuestionDao();
    public abstract AnswerDao getAnswerDao();
    public abstract UserDao getUserDao();
}

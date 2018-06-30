package root.ivatio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import BD.Answer.Answer;
import BD.Answer.AnswerDao;
import BD.Communication.Communication;
import BD.Communication.CommunicationDao;
import BD.Mood.Mood;
import BD.Mood.MoodDao;
import BD.Qustion.Question;
import BD.Qustion.QuestionDao;
import BD.Users.User;
import BD.Users.UserDao;

@Database(entities = {Question.class, Answer.class, User.class, Communication.class, Mood.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
//    public abstract
    public abstract QuestionDao getQuestionDao();
    public abstract AnswerDao getAnswerDao();
    public abstract UserDao getUserDao();
    public abstract CommunicationDao getCommunicationDao();
    public abstract MoodDao getMoodDao();
}

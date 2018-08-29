package root.ivatio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import bd.Answer.Answer;
import bd.Answer.AnswerDao;
import bd.Communication.Communication;
import bd.Communication.CommunicationDao;
import bd.CommunicationKey.CommunicationKey;
import bd.CommunicationKey.CommunicationKeyDao;
import bd.KeyWord.KeyWord;
import bd.KeyWord.KeyWordDao;
import bd.Mood.Mood;
import bd.Mood.MoodDao;
import bd.Qustion.Question;
import bd.Qustion.QuestionDao;
import bd.Users.User;
import bd.Users.UserDao;

@Database(entities = {
        Question.class,
        Answer.class,
        User.class,
        Communication.class,
        Mood.class,
        KeyWord.class,
        CommunicationKey.class},
        version = 2)
public abstract class AppDatabase extends RoomDatabase {
//    public abstract
    public abstract QuestionDao getQuestionDao();
    public abstract AnswerDao getAnswerDao();
    public abstract UserDao getUserDao();
    public abstract CommunicationDao getCommunicationDao();
    public abstract MoodDao getMoodDao();
    public abstract KeyWordDao getKeyWordDao();
    public abstract CommunicationKeyDao getCommunicationKeyDao();
}

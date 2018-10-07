package root.ivatio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import bd.answer.Answer;
import bd.answer.AnswerDao;
import bd.communication.Communication;
import bd.communication.CommunicationDao;
import bd.communication_key.CommunicationKey;
import bd.communication_key.CommunicationKeyDao;
import bd.key_word.KeyWord;
import bd.key_word.KeyWordDao;
import bd.mood.Mood;
import bd.mood.MoodDao;
import bd.qustion.Question;
import bd.qustion.QuestionDao;
import bd.users.User;
import bd.users.UserDao;

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

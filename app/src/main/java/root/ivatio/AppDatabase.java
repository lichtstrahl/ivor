package root.ivatio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import root.ivatio.bd.command.Command;
import root.ivatio.bd.command.CommandDao;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.answer.AnswerDao;
import root.ivatio.bd.communication.Communication;
import root.ivatio.bd.communication.CommunicationDao;
import root.ivatio.bd.communication_key.CommunicationKey;
import root.ivatio.bd.communication_key.CommunicationKeyDao;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.bd.key_word.KeyWordDao;
import root.ivatio.bd.mood.Mood;
import root.ivatio.bd.mood.MoodDao;
import root.ivatio.bd.qustion.Question;
import root.ivatio.bd.qustion.QuestionDao;
import root.ivatio.bd.users.User;
import root.ivatio.bd.users.UserDao;
//
@Database(entities = {
        Question.class,
        Answer.class,
        User.class,
        Communication.class,
        Mood.class,
        KeyWord.class,
        CommunicationKey.class,
        Command.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
//    public abstract
    public abstract QuestionDao getQuestionDao();
    public abstract AnswerDao getAnswerDao();
    public abstract UserDao getUserDao();
    public abstract CommunicationDao getCommunicationDao();
    public abstract MoodDao getMoodDao();
    public abstract KeyWordDao getKeyWordDao();
    public abstract CommunicationKeyDao getCommunicationKeyDao();
    public abstract CommandDao getCommandDao();
}

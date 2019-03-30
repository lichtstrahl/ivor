package root.ivatio.bd.qustion;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import root.ivatio.bd.answer.Answer;

@Dao
public interface QuestionDao {
    @Insert
    long insert(Question q);
    @Insert
    void insert(List<Question> questions);
    @Update
    void update(Question q);
    @Delete
    void delete(Question q);

    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Query("DELETE FROM question")
    void deleteAll();

    @Query("SELECT * FROM Question WHERE content = :content")
    Question getQuestion(String content);

    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestion(long id);

    @Query("SELECT id FROM Question")
    long[] getAllID();

    @Query("SELECT * FROM Answer WHERE id IN ( " +
            "SELECT answerID FROM Communication WHERE questionID = :qID " +
            ")")
    List<Answer> getAnswerForQuestion(long qID);

    @Query("SELECT MAX(id) FROM Question")
    long getMaxID();
}

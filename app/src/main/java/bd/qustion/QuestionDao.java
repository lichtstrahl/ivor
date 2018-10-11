package bd.qustion;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import bd.answer.Answer;

@Dao
public interface QuestionDao {
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

    @Insert
    long insert(Question q);    // id, куда произошла вставка

    @Update
    void update(Question q);

    @Delete
    void delete(Question q);
}

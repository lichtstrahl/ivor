package bd.answer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answer")
    List<Answer> getAll();

    @Query("DELETE FROM Answer")
    void deleteAll();

    @Query("SELECT * FROM ANSWER WHERE id = :id")
    Answer  getAnswer(long id);

    @Query("SELECT * FROM Answer WHERE content = :content")
    Answer getAnswer(String content);

    @Query("SELECT id FROM ANSWER WHERE content = :content")
    long getAnswerID(String content);


    @Update
    void update(Answer a);

    @Insert
    long insert(Answer a);

    @Delete
    int delete(Answer a);
}

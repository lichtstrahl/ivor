package root.ivatio.bd.answer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.IntegerRes;

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

    @Query("SELECT id FROM Answer WHERE content = :content")
    long getAnswerID(String content);

    @Query("SELECT MAX(id) FROM Answer")
    long getMaxID();

    @Update
    void update(Answer a);

    @Insert
    long insert(Answer a);

    @Insert
    void insert(List<Answer> a);

    @Delete
    int delete(Answer a);
}

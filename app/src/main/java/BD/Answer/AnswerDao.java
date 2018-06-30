package BD.Answer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import BD.Answer.Answer;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answer")
    List<Answer> getAll();

    @Query("DELETE FROM Answer")
    void deleteAll();

    @Update
    void update(Answer a);

    @Insert
    long insert(Answer a);

    @Delete
    int delete(Answer a);
}

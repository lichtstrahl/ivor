package root.ivatio.bd.mood;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MoodDao {
    @Insert
    long insert(Mood m);
    @Update
    void update(Mood m);
    @Delete
    int delete(Mood m);

    @Query("DELETE FROM Mood")
    void deleteAll();

    @Query("SELECT * FROM Mood")
    List<Mood> getAll();

    @Query("SELECT * FROM Mood WHERE id=:id")
    Mood getMood(long id);
}

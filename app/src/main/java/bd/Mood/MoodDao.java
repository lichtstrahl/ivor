package bd.Mood;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

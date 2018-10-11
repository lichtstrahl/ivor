package bd.Command;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CommandDao {
    @Insert
    long insert(Command communication);
    @Update
    void update(Command communication);
    @Delete
    int delete(Command communication);

    @Query("SELECT * FROM Command")
    List<Command> getAll();
}

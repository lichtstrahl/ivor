package root.ivatio.bd.command;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommandDao {
    @Insert
    long insert(Command communication);
    @Insert
    void insert(List<Command> commands);
    @Update
    void update(Command communication);
    @Delete
    int delete(Command communication);

    @Query("SELECT * FROM Command")
    List<Command> getAll();

    @Query("DELETE FROM Command")
    void deleteAll();

    @Query("SELECT MAX(id) FROM Communication")
    long getMaxID();
}

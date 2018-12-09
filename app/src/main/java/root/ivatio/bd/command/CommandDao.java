package root.ivatio.bd.command;

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

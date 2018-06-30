package BD.Communication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import BD.Communication.Communication;

@Dao
public interface CommunicationDao {
    @Insert
    long insert(Communication communication);
    @Update
    void update(Communication communication);
    @Delete
    int delete(Communication communication);

    @Query("SELECT * FROM Communication")
    List<Communication> getAll();

    @Query("SELECT * FROM Communication WHERE id = :id")
    Communication getCommunication(long id);

    @Query("DELETE FROM Communication")
    void deleteAll();
}

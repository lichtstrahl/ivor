package bd.CommunicationKey;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CommunicationKeyDao {
    @Query("SELECT * FROM CommunicationKey")
    List<CommunicationKey> getAll();

    @Update
    void update(CommunicationKey communicationKey);
    @Insert
    void insert(CommunicationKey communicationKey);
    @Delete
    void delete(CommunicationKey communicationKey);
}

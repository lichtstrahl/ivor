package bd.communication_key;

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

    @Query("SELECT * FROM COMMUNICATIONKEY WHERE keyID = :kid")
    List<CommunicationKey> getCommunications(long kid);

    @Query("SELECT * FROM COMMUNICATIONKEY WHERE KeyID = :kID AND answerID = :aID")
    CommunicationKey getCommunicationKey(long kID, long aID);

    @Query("DELETE FROM CommunicationKey WHERE id = :id")
    int delete(long id);

    @Update
    void update(CommunicationKey communicationKey);
    @Insert
    void insert(CommunicationKey communicationKey);
    @Delete
    void delete(CommunicationKey communicationKey);
}

package bd.communication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import bd.communication_key.CommunicationKey;

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

    @Query("DELETE FROM Communication WHERE id = :id")
    int delete(long id);

    @Query("SELECT * FROM Communication WHERE questionID = :questionID")
    List<Communication> getCommunication(long questionID);

    @Query("SELECT * FROM COMMUNICATION WHERE questionID = :questionID AND answerID = :answerID")
    Communication getCommunication(long questionID, long answerID);

    @Query("DELETE FROM Communication")
    void deleteAll();
}

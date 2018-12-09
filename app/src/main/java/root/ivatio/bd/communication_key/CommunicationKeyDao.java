package root.ivatio.bd.communication_key;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CommunicationKeyDao {
    @Update
    void update(CommunicationKey communicationKey);
    @Insert
    long insert(CommunicationKey communicationKey);
    @Insert
    void insert(List<CommunicationKey> communicationKeys);
    @Delete
    void delete(CommunicationKey communicationKey);

    @Query("SELECT * FROM CommunicationKey")
    List<CommunicationKey> getAll();

    @Query("SELECT * FROM CommunicationKey WHERE keyID = :kid")
    List<CommunicationKey> getCommunications(long kid);

    @Query("SELECT * FROM CommunicationKey WHERE KeyID = :kID AND answerID = :aID")
    CommunicationKey getCommunicationKey(long kID, long aID);

    @Query("DELETE FROM CommunicationKey WHERE id = :id")
    int delete(long id);

    @Query("SELECT * FROM (SELECT * FROM CommunicationKey ORDER BY correct LIMIT :limit)\n")
    List<CommunicationKey> getWorst(long limit);

    @Query("SELECT COUNT(id) FROM CommunicationKey")
    long getCount();

    @Query("SELECT COUNT(id) FROM Communication WHERE correct < 0")
    long getCountBadAnswer();

    @Query( "DELETE \n" +
            "FROM CommunicationKey\n" +
            "WHERE id IN (\n" +
            "SELECT id\n" +
            "FROM CommunicationKey \n" +
            "WHERE power > (\n" +
            "SELECT AVG(power)\n" +
            "FROM CommunicationKey\n" +
            "WHERE power IS NOT 0\n" +
            ") AND correct < 0\n" +
            "ORDER BY correct LIMIT (\n" +
            "SELECT COUNT(id)/2\n" +
            "FROM CommunicationKey\n" +
            "WHERE correct < 0\n" +
            ")\n" +
            ")")
    void magicalDelete();

    @Query("DELETE FROM CommunicationKey")
    void deleteAll();

    @Query("SELECT * FROM CommunicationKey WHERE answerID = :aID")
    List<CommunicationKey> getCommunicationKeyForAnswer(long aID);
}

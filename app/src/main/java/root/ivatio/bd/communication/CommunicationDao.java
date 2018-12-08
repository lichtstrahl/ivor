package root.ivatio.bd.communication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CommunicationDao {
    @Insert
    long insert(Communication communication);
    @Insert
    void insert(List<Communication> communications);
    @Update
    void update(Communication communication);
    @Delete
    int delete(Communication communication);

    @Query("SELECT * FROM Communication")
    List<Communication> getAll();

    @Query("DELETE FROM Communication WHERE id = :id")
    int delete(long id);

    @Query("SELECT * FROM Communication WHERE questionID = :questionID")
    List<Communication> getCommunicationsForQuestion(long questionID);

    @Query("SELECT * FROM Communication WHERE answerID = :answerID")
    List<Communication> getCommunicationsForAnswer(long answerID);

    @Query("SELECT * FROM Communication WHERE questionID = :questionID AND answerID = :answerID")
    Communication getCommunication(long questionID, long answerID);

    @Query( "DELETE \n" +
            "FROM Communication\n" +
            "WHERE id IN (\n" +
                "SELECT id\n" +
                "FROM Communication\n" +
                "WHERE power > (\n" +
                    "SELECT AVG(power)\n" +
                    "FROM Communication\n" +
                    "WHERE power IS NOT 0\n" +
                ") AND correct < 0\n" +
            "ORDER BY correct LIMIT (\n" +
                "SELECT COUNT(id)/2\n" +
                "FROM Communication\n" +
                "WHERE correct < 0\n" +
            ")\n" +
            ")")
    void magicalDelete();


    @Query("DELETE FROM Communication")
    void deleteAll();
}

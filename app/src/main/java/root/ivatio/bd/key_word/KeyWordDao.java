package root.ivatio.bd.key_word;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import root.ivatio.bd.answer.Answer;

@Dao
public interface KeyWordDao {
    @Insert
    long insert(KeyWord word);
    @Insert
    void insert(List<KeyWord> keyWords);
    @Update
    void update(KeyWord word);
    @Delete
    int delete(KeyWord word);

    @Query("SELECT * FROM KeyWord")
    List<KeyWord> getAll();

    @Query("SELECT * FROM KeyWord WHERE id = :id")
    KeyWord getWord(long id);

    @Query("SELECT * FROM KeyWord WHERE content= :content")
    KeyWord getWord(String content);

    @Query("SELECT id FROM KeyWord WHERE content= :content")
    long getWordID(String content);

    @Query("SELECT id FROM KeyWord")
    long[] getAllID();

    @Query("SELECT * FROM Answer WHERE id IN ( " +
            "SELECT answerID FROM CommunicationKey WHERE keyID = :keyID " +
            ")")
    List<Answer> getAnswerForKeyWord(long keyID);



    @Query("DELETE FROM KeyWord")
    void deleteAll();

    @Query("SELECT MAX(id) FROM KeyWord")
    long getMaxID();
}
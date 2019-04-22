package root.ivatio.bd.key_word;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
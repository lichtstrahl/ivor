package bd.CommunicationKey;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import bd.Answer.Answer;
import bd.KeyWord.KeyWord;

@Entity(foreignKeys = {
        @ForeignKey(entity = KeyWord.class, parentColumns = "id", childColumns = "keyID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class CommunicationKey {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long keyID;
    public long answerID;
    public long power;
    public long correct;

    public CommunicationKey(long keyID, long answerID) {
        this.keyID = keyID;
        this.answerID = answerID;
        this.power = 0;
        this.correct = 0;
    }
}

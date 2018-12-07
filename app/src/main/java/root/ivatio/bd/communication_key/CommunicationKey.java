package root.ivatio.bd.communication_key;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import root.ivatio.bd.CommunicationAPI;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.key_word.KeyWord;

@Entity(foreignKeys = {
        @ForeignKey(entity = KeyWord.class, parentColumns = "id", childColumns = "keyID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class CommunicationKey implements CommunicationAPI {
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

    @Override
    public int getType() {
        return COMMUNICATION_KEY;
    }

    @Override
    public long getID() {
        return id;
    }
}

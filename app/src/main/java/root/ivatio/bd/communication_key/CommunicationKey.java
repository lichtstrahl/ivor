package root.ivatio.bd.communication_key;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.bd.CommunicationAPI;
import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.key_word.KeyWord;
import root.ivatio.network.dto.PostComKeyDTO;

@Entity(foreignKeys = {
        @ForeignKey(entity = KeyWord.class, parentColumns = "id", childColumns = "keyID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class CommunicationKey implements CommunicationAPI {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("keyID")
    public long keyID;
    @SerializedName("answerID")
    public long answerID;
    @SerializedName("power")
    public long power;
    @SerializedName("correct")
    public long correct;

    public CommunicationKey(long keyID, long answerID) {
        this.keyID = keyID;
        this.answerID = answerID;
        this.power = 0;
        this.correct = 0;
    }

    public PostComKeyDTO toDTO() {
        return new PostComKeyDTO(keyID, answerID, power, correct);
    }

    @Override
    public int getType() {
        return COMMUNICATION_KEY;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CommunicationKey && (
                keyID == ((CommunicationKey) o).keyID && answerID == ((CommunicationKey) o).answerID &&
                        power == ((CommunicationKey) o).power && correct == ((CommunicationKey) o).correct
        );
    }
}

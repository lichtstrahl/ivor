package root.ivatio.bd.communication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.qustion.Question;

@Entity(foreignKeys = {
        @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class Communication implements root.ivatio.bd.CommunicationAPI {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("questionID")
    public long questionID;
    @SerializedName("answerID")
    public long answerID;
    @SerializedName("correct")
    public long correct;
    @SerializedName("power")
    public long power;

    public Communication(long questionID, long answerID) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.correct = 0;
        this.power = 0;
    }

    @Override
    public int getType() {
        return COMMUNICATION;
    }

    @Override
    public long getID() {
        return id;
    }
}

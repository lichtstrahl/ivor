package root.ivatio.bd.communication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.qustion.Question;

@Entity(foreignKeys = {
        @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class Communication implements root.ivatio.bd.CommunicationAPI {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long questionID;
    public long answerID;
    public long correct;
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

package bd.Communication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import bd.Answer.Answer;
import bd.Qustion.Question;

@Entity(foreignKeys = {
        @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class Communication {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long questionID;
    public long answerID;
    public long correct;

    public Communication(long questionID, long answerID, long correct) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.correct = correct;
    }
}

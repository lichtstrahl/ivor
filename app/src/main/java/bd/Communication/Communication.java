package bd.Communication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
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

package root.ivatio.bd.communication;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.bd.answer.Answer;
import root.ivatio.bd.qustion.Question;
import root.ivatio.network.dto.PostComDTO;

@Entity(foreignKeys = {
        @ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionID"),
        @ForeignKey(entity = Answer.class, parentColumns = "id", childColumns = "answerID")
})
public class Communication {
    @SerializedName("id")
    @PrimaryKey
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

    public PostComDTO toDTO() {
        return new PostComDTO(questionID, answerID, correct, power);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Communication && (questionID == ((Communication) o).questionID &&
                        answerID == ((Communication) o).answerID && correct == ((Communication) o).correct && power == ((Communication) o).power
        );
    }
}

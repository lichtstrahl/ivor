package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostComDTO {
    @SerializedName("questionID")
    private long questionID;
    @SerializedName("answerID")
    private long answerID;
    @SerializedName("correct")
    private long correct;
    @SerializedName("power")
    private long power;

    public PostComDTO(long questionID, long answerID, long correct, long power) {
        this.questionID = questionID;
        this.answerID = answerID;
        this.correct = correct;
        this.power = power;
    }

    public long getQuestionID() {
        return questionID;
    }

    public long getAnswerID() {
        return answerID;
    }

    public long getCorrect() {
        return correct;
    }

    public long getPower() {
        return power;
    }
}

package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostComDTO {
    @SerializedName("questionID")
    private long questionID;
    @SerializedName("answerID")
    private long answerID;
    @SerializedName("correct")
    private int correct;
    @SerializedName("power")
    private int power;

    public long getQuestionID() {
        return questionID;
    }

    public long getAnswerID() {
        return answerID;
    }

    public int getCorrect() {
        return correct;
    }

    public int getPower() {
        return power;
    }
}

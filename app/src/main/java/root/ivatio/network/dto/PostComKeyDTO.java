package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostComKeyDTO {
    @SerializedName("keyID")
    private long keyID;
    @SerializedName("answerID")
    private long answerID;
    @SerializedName("power")
    private int power;
    @SerializedName("correct")
    private int correct;

    public long getKeyID() {
        return keyID;
    }

    public long getAnswerID() {
        return answerID;
    }

    public int getPower() {
        return power;
    }

    public int getCorrect() {
        return correct;
    }
}

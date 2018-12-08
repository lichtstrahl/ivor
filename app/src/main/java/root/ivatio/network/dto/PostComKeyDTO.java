package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostComKeyDTO {
    @SerializedName("keyID")
    private long keyID;
    @SerializedName("answerID")
    private long answerID;
    @SerializedName("power")
    private long power;
    @SerializedName("correct")
    private long correct;

    public PostComKeyDTO(long keyID, long answerID, long power, long correct) {
        this.keyID = keyID;
        this.answerID = answerID;
        this.power = power;
        this.correct = correct;
    }

    public long getKeyID() {
        return keyID;
    }

    public long getAnswerID() {
        return answerID;
    }

    public long getPower() {
        return power;
    }

    public long getCorrect() {
        return correct;
    }
}

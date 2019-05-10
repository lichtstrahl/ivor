package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class AnswerForQuestionDTO {
    @SerializedName("answer")
    private String answer;
    @SerializedName("question_id")
    private long questionID;

    public String getAnswer() {
        return answer;
    }

    public long getQuestionID() {
        return questionID;
    }
}

package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class AnswerForQuestionDTO {
    @SerializedName("answer")
    private String answer;
    @SerializedName("question_id")
    private Long questionID;

    public String getAnswer() {
        return answer;
    }

    public Long getQuestionID() {
        return questionID;
    }
}

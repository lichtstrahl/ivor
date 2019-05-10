package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class EvaluationDTO {
    @SerializedName("question_id")
    private long questionID;
    @SerializedName("eval")
    private int eval;

    public EvaluationDTO(long qID, int e) {
        questionID = qID;
        eval = e;
    }

    public long getQuestionID() {
        return questionID;
    }

    public int getEval() {
        return eval;
    }
}

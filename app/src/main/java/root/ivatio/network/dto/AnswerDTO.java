package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class AnswerDTO {
    @SerializedName("answer")
    private String answer;
    @SerializedName("communication")
    private long communicationID;
    @SerializedName("communication_key")
    private long communicationKeyID;

    public String getAnswer() {
        return answer;
    }

    public long getCommunicationID() {
        return communicationID;
    }

    public long getCommunicationKeyID() {
        return communicationKeyID;
    }
}

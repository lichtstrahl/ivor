package root.ivatio.network.dto;

import com.google.gson.internal.LinkedTreeMap;

import lombok.Data;

@Data
public class AnswerDTO {
    private static final String ANSWER_JSON = "answer";
    private String answer;
    private static final String COMMUNICATION_ID_JSON = "communication";
    private long communicationID;
    private static final String COMMUNICATIONKEY_ID_JSON = "communication_key";
    private long communicationKeyID;
    private AnswerType type;

    private AnswerDTO() {}

    public static AnswerDTO parseJSON(LinkedTreeMap<String, Object> tree) {
        AnswerDTO answer = new AnswerDTO();
        answer.answer = (String) tree.get(ANSWER_JSON);

        Double com = (Double) tree.get(COMMUNICATION_ID_JSON);
        if (com != null) {
            answer.communicationID = Math.round(com);
            answer.type = AnswerType.COMMUNICATION;
        }

        Double comKey = (Double) tree.get(COMMUNICATIONKEY_ID_JSON);
        if (comKey != null) {
            answer.communicationKeyID = Math.round(comKey);
            answer.type = AnswerType.COMMUNICATION_KEY;
        }

        if (comKey == null && com == null) {
            answer.type = AnswerType.UNDEFINED;
        }

        return answer;
    }

    public enum AnswerType {
        COMMUNICATION,
        COMMUNICATION_KEY,
        UNDEFINED
    }
}

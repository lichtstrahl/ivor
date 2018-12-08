package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostCommandDTO {
    @SerializedName("cmd")
    private String content;

    public PostCommandDTO(String c) {
        content = c;
    }

    public String getContent() {
        return content;
    }
}

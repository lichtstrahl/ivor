package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostContentDTO {
    @SerializedName("content")
    private String content;

    public PostContentDTO(String c) {
        content = c;
    }

    public String getContent() {
        return content;
    }
}

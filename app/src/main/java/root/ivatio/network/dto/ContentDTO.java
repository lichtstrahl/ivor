package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class ContentDTO {
    @SerializedName("content")
    private String content;

    public ContentDTO(String c) {
        content = c;
    }

    public String getContent() {
        return content;
    }
}

package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostContentDTO {
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }
}

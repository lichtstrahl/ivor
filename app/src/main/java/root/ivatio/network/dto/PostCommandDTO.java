package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class PostCommandDTO {
    @SerializedName("cmd")
    private String content;

    public String getContent() {
        return content;
    }
}

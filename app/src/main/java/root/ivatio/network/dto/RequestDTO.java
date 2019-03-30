package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class RequestDTO {
    @SerializedName("request")
    private String requet;

    public RequestDTO(String requet) {
        this.requet = requet;
    }
}

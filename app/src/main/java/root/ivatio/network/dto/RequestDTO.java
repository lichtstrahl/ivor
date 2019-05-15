package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class RequestDTO {
    @SerializedName("request")
    private String request;

    public static RequestDTO fromString(String r) {
        RequestDTO request = new RequestDTO();
        request.setRequest(r);
        return request;
    }
}

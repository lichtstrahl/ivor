package root.ivatio.network.dto;

import com.google.gson.annotations.SerializedName;

public class ServerAnswerDTO<T> {
    @SerializedName("error")
    private int error;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private T data;

    public int getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}

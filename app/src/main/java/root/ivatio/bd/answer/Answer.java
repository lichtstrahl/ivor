package root.ivatio.bd.answer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.network.dto.PostContentDTO;

@Entity
public class Answer {
    @SerializedName("id")
    @PrimaryKey
    public long id;
    @SerializedName("content")
    public String content;
    public Answer(String content) {
        this.content = content;
    }

    public PostContentDTO toDTO() {
        return new PostContentDTO(content);
    }

    @Override
    public boolean equals(Object o) {
            return o instanceof Answer && content.equals(((Answer) o).content);
    }
}

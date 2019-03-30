package root.ivatio.bd.qustion;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.network.dto.PostContentDTO;

@Entity
public class Question {
    @SerializedName("id")
    @PrimaryKey
    public long id;
    @SerializedName("content")
    public String content;

    public Question(String content) {
        this.content = content;
    }

    public PostContentDTO toDTO() {
        return new PostContentDTO(content);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Question && content.equals(((Question)o).content);
    }
}

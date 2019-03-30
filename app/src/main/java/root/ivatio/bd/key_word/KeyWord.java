package root.ivatio.bd.key_word;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import root.ivatio.network.dto.PostContentDTO;

@Entity
public class KeyWord {
    @SerializedName("id")
    @PrimaryKey
    public long id;
    @SerializedName("content")
    @NonNull
    public String content;

    public KeyWord(String content) {
        this.content = content;
    }

    public PostContentDTO toDTO() {
        return new PostContentDTO(content);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof KeyWord && content.equals(((KeyWord)o).content);
    }
}

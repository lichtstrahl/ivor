package root.ivatio.bd.key_word;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class KeyWord {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("content")
    @NonNull
    public String content;

    public KeyWord(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return content.equals(((KeyWord)obj).content);
    }
}

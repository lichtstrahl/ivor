package root.ivatio.bd.qustion;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Question {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("content")
    public String content;

    public Question(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return content.equals(((Question)obj).content);
    }
}

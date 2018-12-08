package root.ivatio.bd.answer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Answer {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("content")
    public String content;
    public Answer(String content) {
        this.content = content;
    }
}

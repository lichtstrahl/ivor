package root.ivatio.bd.answer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String content;
    public Answer(String content) {
        this.content = content;
    }
}
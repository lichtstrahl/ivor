package bd.qustion;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Question {
    @PrimaryKey(autoGenerate = true)
    public long id;
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

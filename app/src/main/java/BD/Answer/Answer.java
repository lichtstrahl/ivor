package BD.Answer;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String content;
    public int power;

    public Answer(String content) {
        this.content = content;
        this.power = 0;
    }
}

package bd.Qustion;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Question {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String content;
    public int power;

    public Question(String content) {
        this.content = content;
        this.power = 0;
    }
}

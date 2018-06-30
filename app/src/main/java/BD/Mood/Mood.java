package BD.Mood;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Mood {
    @PrimaryKey
    public long id;
    public long answerID;
    public long mood;

    public Mood(long answerID, long mood) {
        this.answerID = answerID;
        this.mood = mood;
    }
}

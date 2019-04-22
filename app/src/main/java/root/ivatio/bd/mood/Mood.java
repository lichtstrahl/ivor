package root.ivatio.bd.mood;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

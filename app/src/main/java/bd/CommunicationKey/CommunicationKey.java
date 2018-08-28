package bd.CommunicationKey;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CommunicationKey {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long keyID;
    public long answerID;

    public CommunicationKey(long keyID, long answerID) {
        this.keyID = keyID;
        this.answerID = answerID;
    }
}

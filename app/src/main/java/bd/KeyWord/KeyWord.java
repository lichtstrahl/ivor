package bd.KeyWord;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//@Entity(indices = @Index(value = "id", unique = true))
@Entity
public class KeyWord {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String content;

    public KeyWord(String content) {
        this.content = content;
    }
}

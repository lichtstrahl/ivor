package bd.key_word;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class KeyWord {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @NonNull
    public String content;

    public KeyWord(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        return content.equals(((KeyWord)obj).content);
    }
}

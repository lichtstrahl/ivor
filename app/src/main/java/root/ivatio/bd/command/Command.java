package root.ivatio.bd.command;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Command {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String cmd;

    public Command(String cmd) {
        this.cmd = cmd;
    }
}

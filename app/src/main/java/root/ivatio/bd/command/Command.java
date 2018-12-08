package root.ivatio.bd.command;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Command {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("cmd")
    public String cmd;

    public Command(String cmd) {
        this.cmd = cmd;
    }
}

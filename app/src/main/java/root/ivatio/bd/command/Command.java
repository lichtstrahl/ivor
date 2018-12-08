package root.ivatio.bd.command;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import root.ivatio.network.dto.PostCommandDTO;

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

    public PostCommandDTO toDTO() {
        return new PostCommandDTO(cmd);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Command && cmd.equals(((Command) o).cmd);
    }
}

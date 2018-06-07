package root.ivatio;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey
    public long id;
    public String name;
    public int salary;
}

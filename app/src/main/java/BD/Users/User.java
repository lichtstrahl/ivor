package BD.Users;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String realName = null;
    public String login = null;
    public String pass = null;
    public int age = -1;
    public String city = null;
    public String email = null;
    public String lastEntry = null;

    public static UserBuilder getUserBuilder() {
        return new User(). new UserBuilder();
    }

    public class UserBuilder {
        public UserBuilder buildName(String name) {
            realName = name;
            return this;
        }
        public UserBuilder buildLogin(String login) {
            User.this.login = login;
            return this;
        }
        public UserBuilder buildPassword(String password) {
            User.this.pass = password;
            return this;
        }
        public UserBuilder buildAge(int age) {
            User.this.age = age;
            return this;
        }
        public UserBuilder buildCity(String city) {
            User.this.city = city;
            return this;
        }
        public UserBuilder buildEmail(String email) {
            User.this.email = email;
            return this;
        }
        public UserBuilder buildTimeEntry() {
            User.this.lastEntry = Calendar.getInstance().getTime().toString();
            return this;
        }

        public User build() {
            if (lastEntry != null && email != null && city != null &&
                    age != -1 && pass != null && login != null && realName != null)
                return User.this;
            else
                return null;
        }
    }

}


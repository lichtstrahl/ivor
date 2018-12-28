package root.ivatio.bd.users;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

@Entity
public class User implements Serializable {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    public long id;
    @SerializedName("realName")
    public String realName = null;
    @SerializedName("login")
    public String login = null;
    @SerializedName("pass")
    public String pass = null;
    @SerializedName("age")
    public Integer age = null;
    @SerializedName("city")
    public String city = null;
    @SerializedName("email")
    public String email = null;
    @SerializedName("lastEntry")
    public String lastEntry = null;
    @SerializedName("admin")
    public Integer admin = null;

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

    public boolean isAdmin() {
        return admin != null;
    }

    public static class PostUser {
        @SerializedName("realName")
        public String realName;
        @SerializedName("login")
        public String login;
        @SerializedName("pass")
        public String pass;
        @SerializedName("age")
        public Integer age;
        @SerializedName("city")
        public String city;
        @SerializedName("email")
        public String email;
        @SerializedName("lastEntry")
        public String lastEntry;
        @SerializedName("admin")
        public Integer admin;

        public PostUser(User user) {
            this.admin = user.admin;
            this.age = user.age;
            this.city = user.city;
            this.email = user.email;
            this.lastEntry = user.lastEntry;
            this.login = user.login;
            this.pass = user.pass;
            this.realName = user.realName;
        }
    }
}


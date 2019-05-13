package root.ivatio.bd.users;

import com.google.gson.annotations.SerializedName;

public class UserPost {
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

    public static UserPost fromUser(User usr) {
        UserPost u = new UserPost();
        u.realName = usr.realName;
        u.login = usr.login;
        u.pass = usr.pass;
        u.age = usr.age;
        u.city = usr.city;
        u.email = usr.email;
        u.lastEntry = usr.lastEntry;
        u.admin = usr.admin;
        return u;
    }
}

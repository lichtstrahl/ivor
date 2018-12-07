package root.ivatio.bd.users;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(long id);

    @Query("SELECT * FROM User WHERE login = :login AND pass = :password")
    User getUser(String login, String password);


    @Insert
    void insert(User u);
    @Delete
    void delete(User u);
    @Update
    void update(User u);
}

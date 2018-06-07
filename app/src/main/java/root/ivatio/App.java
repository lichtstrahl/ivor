package root.ivatio;

import android.app.Application;
import android.arch.persistence.room.Room;

public class App extends Application {
    private static AppDatabase DB;
    private static String nameDB = "database";
    public static AppDatabase getDB() {
        return DB;
    }

    public static String getDBName() {
        return nameDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DB = Room.databaseBuilder(this, AppDatabase.class, nameDB).allowMainThreadQueries().build();
    }
}

package root.ivatio;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.facebook.stetho.Stetho;

public class App extends Application {
    private static AppDatabase DB;
    private static String nameDB = "database";
    public static AppDatabase getDB() {
        return DB;
    }
    public static String getDBName() {
        return nameDB;
    }

    public static long SUPER_USER_INDEX = 2;

    // Коды для передачи парметров между Activity
    public static final String USER_INDEX = "USER_INDEX";

    @Override
    public void onCreate() {
        super.onCreate();
        DB = Room.databaseBuilder(this, AppDatabase.class, nameDB).allowMainThreadQueries().build();
        Stetho.initializeWithDefaults(this);
    }
}

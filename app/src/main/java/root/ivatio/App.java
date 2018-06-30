package root.ivatio;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

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
//  Здесь может быть расположен код для миграций, но пока
//    public static final Migration migration_12 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("");
//        }
//    };


    // Коды для передачи парметров между Activity
    public static final String USER_INDEX = "USER_INDEX";

    @Override
    public void onCreate() {
        super.onCreate();
        DB = Room.databaseBuilder(this, AppDatabase.class, nameDB).allowMainThreadQueries().fallbackToDestructiveMigration().build();

        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.initialize(builder.build());

//        Stetho.initializeWithDefaults(this);
    }
}

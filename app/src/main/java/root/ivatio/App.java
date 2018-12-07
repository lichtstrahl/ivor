package root.ivatio;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.Stetho;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import root.ivatio.network.UserAPI;
import root.ivatio.util.LocalStorageAPI;
import root.ivatio.util.StorageAPI;

public class App extends Application {
    private static AppDatabase db;
    private static String nameDB = "database";
    public static final String USER_INDEX = "USER_INDEX";
    private static final LocalStorageAPI localAPI = new LocalStorageAPI();
    private static Retrofit retrofit;
    private static UserAPI userAPI;

    private final Migration migration12 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            createTriggers(database);
           }
    };

    private final Migration migration21 = new Migration(2, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            createTriggers(database);
        }
    };

    private void createTriggers(SupportSQLiteDatabase database) {
        database.query("CREATE TRIGGER IF NOT EXISTS \"deleteAnswer\" AFTER DELETE ON Answer FOR EACH ROW Begin DELETE FROM Communication WHERE answerID = OLD.id; DELETE FROM CommunicationKey WHERE answerID = OLD.id; END\t");
        database.query("CREATE TRIGGER IF NOT EXISTS \"deleteKeyWord\" AFTER DELETE ON KeyWord FOR EACH ROW Begin DELETE FROM CommunicationKey WHERE keyID = OLD.id; END\t");
        database.query("CREATE TRIGGER IF NOT EXISTS \"deleteQuestion\" AFTER DELETE ON Question FOR EACH ROW Begin DELETE FROM Communication WHERE questionID = OLD.id; END\t");
        // Удаление Answer-ов, на которые больше нет ссылок, так как больше нет способов создать эти ссылки
        database.query("CREATE TRIGGER IF NOT EXISTS \"deleteCommunication\" AFTER DELETE ON Communication FOR EACH ROW \n" +
                "Begin \n" +
                "DELETE FROM Answer\n" +
                "WHERE id  NOT IN (\n" +
                    "SELECT answerID\n" +
                    "FROM Communication\n" +
                ") AND id NOT IN (\n" +
                    "SELECT answerID \n" +
                    "FROM CommunicationKey\n" +
                ");\n" +
                "END\t");
        database.query("CREATE TRIGGER IF NOT EXISTS \"deleteCommunicationKey\" AFTER DELETE ON CommunicationKey FOR EACH ROW \n" +
                "Begin \n" +
                "DELETE FROM Answer\n" +
                "WHERE id  NOT IN (\n" +
                    "SELECT answerID\n" +
                    "FROM Communication\n" +
                ") AND id NOT IN (\n" +
                    "SELECT answerID \n" +
                    "FROM CommunicationKey\n" +
                ");\n" +
                "END\t");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(this, AppDatabase.class, nameDB)
                .allowMainThreadQueries()
                .addMigrations(migration12, migration21)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI = retrofit.create(UserAPI.class);


        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.initialize(builder.build());
    }

    public static AppDatabase getDB() {
        return db;
    }

    public static StorageAPI getStorageAPI() {
        return localAPI;
    }

    public static UserAPI getUserAPI() {
        return userAPI;
    }


    public static void logI(String msg) {
        Log.i(BuildConfig.GLOBAL_TAG, msg);
    }

    public static void logW(String msg) {
        Log.w(BuildConfig.GLOBAL_TAG, msg);
    }

    public static void logE(String msg) {
        Log.e(BuildConfig.GLOBAL_TAG, msg);
    }
}

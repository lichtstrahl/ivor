package root.ivatio;

import android.app.Application;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.Stetho;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import root.ivatio.network.api.LoadAPI;
import root.ivatio.network.api.PythonServerAPI;
import root.ivatio.network.api.UserAPI;
import root.ivatio.util.LocalStorageAPI;

public class App extends Application {
    private static AppDatabase db;
    private static String nameDB = "database";
    public static final String USER_INDEX = "USER_INDEX";
    private static final LocalStorageAPI localAPI = new LocalStorageAPI();
    private static Retrofit retrofit;
    private static UserAPI userAPI;
    private static LoadAPI loadAPI;
    private static PythonServerAPI serverAPI;

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
//                .fallbackToDestructiveMigration()
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        userAPI = retrofit.create(UserAPI.class);
        loadAPI = retrofit.create(LoadAPI.class);
        serverAPI = retrofit.create(PythonServerAPI.class);


        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.initialize(builder.build());
    }

    /**
     * @deprecated - запрещен к использованию, так как БД была вынесена насервер Heroku
     * @return - экземпляр БД
     */
    @Deprecated
    public static AppDatabase getDB() {
        return db;
    }

    public static LocalStorageAPI getStorageAPI() {
        return localAPI;
    }

    public static UserAPI getUserAPI() {
        return userAPI;
    }

    public static PythonServerAPI getServerAPI() {
        return serverAPI;
    }

    public static LoadAPI getLoadAPI() {
        return loadAPI;
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

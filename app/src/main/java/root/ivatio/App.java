package root.ivatio;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.migration.Migration;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class App extends Application {
    private static AppDatabase DB;
    private static String nameDB = "database";
    public static AppDatabase getDB() {
        return DB;
    }
    public static String getDBName() {
        return nameDB;
    }
    // Коды для передачи парметров между Activity
    public static final String USER_INDEX = "USER_INDEX";

    private final Migration migration_12 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        DB = Room.databaseBuilder(this, AppDatabase.class, nameDB)
                .allowMainThreadQueries()
                .addMigrations(migration_12)
                .build();

        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.initialize(builder.build());
    }


    public static List<String> getTables() {
        SupportSQLiteQuery query = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "SELECT tbl_name FROM sqlite_master\n" +
                        "WHERE type=\"table\"";
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {

            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        Cursor c = DB.query(query);
        List<String> list = new ArrayList<>();

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                list.add(c.getString(0));
                c.moveToNext();
            }
        }
        return list;
    }

}

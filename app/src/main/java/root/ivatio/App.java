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
    // Добавление таблицы KeyWord
    private final Migration migration_12 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE \"KeyWord\";");
            database.execSQL("CREATE TABLE \"KeyWord\"(\n" +
                    "\t\"id\" Integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"content\" Text NOT NULL,\n" +
                    "\t\"answerID\" Integer NOT NULL,\n" +
                    "\tCONSTRAINT \"lnk_KeyWord_KeyWord\" FOREIGN KEY ( \"answerID\" ) REFERENCES \"Answer\"( \"id\" )\n" +
                    ",\n" +
                    "CONSTRAINT \"unique_answerID\" UNIQUE ( \"answerID\" ) );");
        }
    };
    private final Migration migration_23 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE \"__vs_temp_table\"(\n" +
                    "\t\"id\"       Integer NOT NULL PRIMARY KEY,\n" +
                    "\t\"content\"  Text NOT NULL,\n" +
                    "\t\"answerID\" Integer NOT NULL,\n" +
                    "\t\"correct\"  Integer NOT NULL,\n" +
                    "CONSTRAINT \"unique_id\" UNIQUE ( \"id\" ) );\n" +
                    "\n" +
                    "INSERT INTO __vs_temp_table(\"id\", \"content\", \"answerID\")\n" +
                    "\tSELECT \"id\", \"content\", \"answerID\" FROM \"KeyWord\";\n" +
                    "\n" +
                    "DROP TABLE IF EXISTS \"KeyWord\";\n" +
                    "\n" +
                    "ALTER TABLE __vs_temp_table RENAME TO \"KeyWord\";");
        }
    };
    private final Migration migration_34 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    private final Migration migration_45 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE \"CommunicationKey\"(\n" +
                    "\t\"id\" Integer NOT NULL PRIMARY KEY,\n" +
                    "\t\"keyID\" Integer NOT NULL,\n" +
                    "\t\"answerID\" Integer NOT NULL );");
        }
    };
    private final Migration migration_56 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("" +
                    "CREATE TABLE t1_backup(id,content);\n" +
                    "INSERT INTO t1_backup SELECT id,content FROM KeyWord;\n" +
                    "DROP TABLE KeyWord;\n" +
                    "CREATE TABLE KeyWord(id, content);\n" +
                    "INSERT INTO KeyWord SELECT id, content FROM t1_backup;\n" +
                    "DROP TABLE t1_backup;" +
                    "");
        }
    };
    private final Migration migration_67 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("" +
                    "ALTER TABLE KeyWord ADD power INTEGER NOT NULL DEFAULT '0'" +
                    "");
        }
    };

    // Коды для передачи парметров между Activity
    public static final String USER_INDEX = "USER_INDEX";

    @Override
    public void onCreate() {
        super.onCreate();
        DB = Room.databaseBuilder(this, AppDatabase.class, nameDB)
                .allowMainThreadQueries()
                .addMigrations(migration_12, migration_23, migration_34, migration_45, migration_56, migration_67)
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

package root.ivatio;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private static AppDatabase db;
    private static String nameDB = "database";
    public static AppDatabase getDB() {
        return db;
    }
    public static String getDBName() {
        return nameDB;
    }
    // Коды для передачи парметров между Activity
    public static final String USER_INDEX = "USER_INDEX";

    private final Migration migration12 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Пустая миграция
        }
    };
    private final Migration migration21 = new Migration(2, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Пустая миграция
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(this, AppDatabase.class, nameDB)
                .allowMainThreadQueries()
                .addMigrations(migration12, migration21)
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
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        Cursor c = db.query(query);
        List<String> list = new ArrayList<>();

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                list.add(c.getString(0));
                c.moveToNext();
            }
        }
        return list;
    }

    public static void deleteDuplicates(final String tableName, final String[] col) {
        final String bufTable = tableName+"_buf";
        SupportSQLiteQuery copyTable = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "CREATE TABLE " + bufTable + " AS SELECT * FROM " + tableName;
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        SupportSQLiteQuery clearTable = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "DELETE FROM " + bufTable;
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        SupportSQLiteQuery insert = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                String columns = "";
                for (int i = 0; i < col.length-1; i++)
                    col[i] = col[i].concat(", ");

                StringBuilder builder = new StringBuilder();
                for (String s : col)
                    builder.append(s);
                columns = builder.toString();


                return "INSERT INTO " + bufTable + " SELECT * FROM " + tableName + "GROUP BY "+ columns +" \n";
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        SupportSQLiteQuery delOld = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "DROP TABLE " + tableName;
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };
        SupportSQLiteQuery rename = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "ALTER TABLE " + bufTable + "RENAME TO " + tableName;
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {
                // Здесь ничего не происходит, потому что я не знаю, где это используется
            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };

        db.query(copyTable);
        db.query(clearTable);
        db.query(insert);
        db.query(delOld);
        db.query(rename);
    }

}

package root.ivatio;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.Stetho;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import root.ivatio.network.api.LoadAPI;
import root.ivatio.network.api.UserAPI;

public class App extends Application {
    private static Retrofit retrofit;
    private static UserAPI userAPI;
    private static LoadAPI loadAPI;
    @Override
    public void onCreate() {
        super.onCreate();

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


        Stetho.InitializerBuilder builder = Stetho.newInitializerBuilder(this);
        builder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        builder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));
        Stetho.initialize(builder.build());
    }

    public static UserAPI getUserAPI() {
        return userAPI;
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

package pl.eduweb.podcastplayer;

import android.app.Application;
import android.preference.PreferenceManager;

import com.squareup.otto.Bus;

import java.io.IOException;
import java.sql.SQLException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.eduweb.podcastplayer.api.ErrorConverter;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.db.DbHelper;
import pl.eduweb.podcastplayer.di.AppComponent;
import pl.eduweb.podcastplayer.di.AppModule;
import pl.eduweb.podcastplayer.di.DaggerAppComponent;
import pl.eduweb.podcastplayer.screens.discover.DiscoverManager;
import pl.eduweb.podcastplayer.screens.login.LoginManager;
import pl.eduweb.podcastplayer.screens.register.RegisterManager;
import pl.eduweb.podcastplayer.screens.subscribed.SubscribedManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Autor on 2016-07-06.
 */
public class App extends Application {

    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

}

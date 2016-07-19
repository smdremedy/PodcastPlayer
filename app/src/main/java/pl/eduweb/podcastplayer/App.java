package pl.eduweb.podcastplayer;

import android.app.Application;

import pl.eduweb.podcastplayer.di.AppComponent;
import pl.eduweb.podcastplayer.di.AppModule;
import pl.eduweb.podcastplayer.di.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {

    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

}

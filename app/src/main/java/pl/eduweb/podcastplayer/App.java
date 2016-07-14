package pl.eduweb.podcastplayer;

import android.app.Application;
import android.preference.PreferenceManager;

import com.squareup.otto.Bus;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.screens.login.LoginManager;
import pl.eduweb.podcastplayer.screens.register.RegisterManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Autor on 2016-07-06.
 */
public class App extends Application {

    private LoginManager loginManager;
    private RegisterManager registerManager;
    private UserStorage userStorage;
    private PodcastApi podcastApi;
    private Retrofit retrofit;
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://podcastplayereduweb.herokuapp.com/parse/");
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(client);
        retrofit = builder.build();
        podcastApi = retrofit.create(PodcastApi.class);

        userStorage = new UserStorage(PreferenceManager.getDefaultSharedPreferences(this));
        loginManager = new LoginManager(userStorage, podcastApi, retrofit);
        registerManager = new RegisterManager(userStorage, podcastApi, retrofit);

        bus = new Bus();

    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public PodcastApi getPodcastApi() {
        return podcastApi;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Bus getBus() {
        return bus;
    }
}

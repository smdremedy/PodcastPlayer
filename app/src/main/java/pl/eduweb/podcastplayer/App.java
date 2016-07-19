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
import pl.eduweb.podcastplayer.screens.discover.DiscoverManager;
import pl.eduweb.podcastplayer.screens.login.LoginManager;
import pl.eduweb.podcastplayer.screens.register.RegisterManager;
import pl.eduweb.podcastplayer.screens.subscribed.SubscribedManager;
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
    private DiscoverManager discoverManager;
    private ErrorConverter errorConverter;
    private SubscribedManager subscribedManager;
    private DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder()
                                .addHeader("X-Parse-Application-Id", "podcastplayereduweb")
                                .addHeader("X-Parse-REST-API-Key", "undefined")
                                .addHeader("X-Parse-Revocable-Session", "1").build();
                        return chain.proceed(newRequest);
                    }
                })
                .addNetworkInterceptor(loggingInterceptor).build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://podcastplayereduweb.herokuapp.com/parse/");
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(client);
        retrofit = builder.build();
        podcastApi = retrofit.create(PodcastApi.class);
        bus = new Bus();

        dbHelper = new DbHelper(this);
        userStorage = new UserStorage(PreferenceManager.getDefaultSharedPreferences(this));
        errorConverter = new ErrorConverter(retrofit);
        loginManager = new LoginManager(userStorage, podcastApi, errorConverter);
        registerManager = new RegisterManager(userStorage, podcastApi, retrofit);
        discoverManager = new DiscoverManager(podcastApi, bus, userStorage, errorConverter);
        try {
            subscribedManager = new SubscribedManager(podcastApi, userStorage,dbHelper, bus);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public SubscribedManager getSubscribedManager() {
        return subscribedManager;
    }

    public DiscoverManager getDiscoverManager() {
        return discoverManager;
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

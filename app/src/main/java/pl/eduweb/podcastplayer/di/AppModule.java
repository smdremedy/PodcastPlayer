package pl.eduweb.podcastplayer.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.otto.Bus;

import java.io.IOException;
import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.eduweb.podcastplayer.UserStorage;
import pl.eduweb.podcastplayer.api.ErrorConverter;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.db.DbHelper;
import pl.eduweb.podcastplayer.db.PodcastDao;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import pl.eduweb.podcastplayer.screens.login.LoginManager;
import pl.eduweb.podcastplayer.screens.register.RegisterManager;
import pl.eduweb.podcastplayer.screens.subscribed.SubscribedManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }




    @Provides
    public Retrofit provideRetrofit() {
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
        return builder.build();
    }

    @Provides
    public PodcastApi providePodcastApi(Retrofit retrofit) {
        return retrofit.create(PodcastApi.class);
    }


    @Provides
    public DbHelper provideDbHelper(Context context) {
        return new DbHelper(context);
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    public UserStorage provideUserStorage(SharedPreferences sharedPreferences) {
        return new UserStorage(sharedPreferences);
    }

    @Singleton
    @Provides
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    public ErrorConverter provideErrorConverter(Retrofit retrofit) {
        return new ErrorConverter(retrofit);
    }

    @Provides
    public PodcastDao providePodcastDao(DbHelper dbHelper) {
        try {
            return dbHelper.getDao(PodcastInDb.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't create dao!", e);
        }
    }

    @Singleton
    @Provides
    public LoginManager provideLoginManager(UserStorage userStorage, PodcastApi podcastApi, ErrorConverter errorConverter) {
        return new LoginManager(userStorage, podcastApi, errorConverter);
    }

    @Singleton
    @Provides
    public RegisterManager provideRegisterManager(UserStorage userStorage, PodcastApi podcastApi, Retrofit retrofit) {
        return new RegisterManager(userStorage, podcastApi, retrofit);
    }

    @Singleton
    @Provides
    public SubscribedManager provideSubscribedManager(PodcastApi podcastApi, UserStorage userStorage, PodcastDao dao, Bus bus) {
        return new SubscribedManager(podcastApi, userStorage, dao, bus);
    }


}


package pl.eduweb.podcastplayer.screens.login;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import pl.eduweb.podcastplayer.UserStorage;
import pl.eduweb.podcastplayer.api.ErrorConverter;
import pl.eduweb.podcastplayer.api.ErrorResponse;
import pl.eduweb.podcastplayer.api.UserResponse;
import pl.eduweb.podcastplayer.api.PodcastApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Autor on 2016-07-06.
 */
public class LoginManager {

    private LoginActivity loginActivity;
    private final UserStorage userStorage;
    private final PodcastApi podcastApi;
    private final ErrorConverter converter;
    private Call<UserResponse> loginCall;

    public LoginManager(UserStorage userStorage, PodcastApi podcastApi, ErrorConverter converter) {
        this.userStorage = userStorage;
        this.podcastApi = podcastApi;
        this.converter = converter;
    }

    public void onAttach(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        updateProgress();
    }

    public void onStop() {
        this.loginActivity = null;
    }

    public void login(final String username, String password) {

        if (loginCall == null) {
            loginCall = podcastApi.getLogin(username, password);
            updateProgress();
            loginCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    loginCall = null;
                    updateProgress();
                    if (response.isSuccessful()) {
                        UserResponse body = response.body();
                        Log.d(LoginActivity.class.getSimpleName(), "Resp: " + body.toString());
                        userStorage.save(body);
                        if (loginActivity != null) {
                            loginActivity.loginSuccess();

                        }
                    } else {
                        ResponseBody responseBody = response.errorBody();

                        ErrorResponse errorResponse = converter.convert(responseBody);
                        if (loginActivity != null && errorResponse != null) {
                            loginActivity.showError(errorResponse.error);

                        }
                    }


                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    loginCall = null;
                    updateProgress();
                    if (loginActivity != null) {
                        loginActivity.showError(t.getLocalizedMessage());

                    }

                }
            });
        }
    }

    private void updateProgress() {
        if (loginActivity != null) {
            loginActivity.showProgress(loginCall != null);
        }
    }
}

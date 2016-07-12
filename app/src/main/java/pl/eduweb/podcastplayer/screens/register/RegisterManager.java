package pl.eduweb.podcastplayer.screens.register;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import pl.eduweb.podcastplayer.UserStorage;
import pl.eduweb.podcastplayer.api.ErrorResponse;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.api.RegisterRequest;
import pl.eduweb.podcastplayer.api.UserResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Autor on 2016-07-06.
 */
public class RegisterManager {

    private RegisterActivity registerActivity;
    private final UserStorage userStorage;
    private final PodcastApi podcastApi;
    private final Retrofit retrofit;
    private Call<UserResponse> userResponseCall;


    public RegisterManager(UserStorage userStorage, PodcastApi podcastApi, Retrofit retrofit) {
        this.userStorage = userStorage;
        this.podcastApi = podcastApi;
        this.retrofit = retrofit;
    }

    public void onAttach(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        updateProgress();
    }

    public void onStop() {
        this.registerActivity = null;
    }


    public void register(final String firstName, final String lastName, final String email, String password) {
        final RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.firstName = firstName;
        registerRequest.lastName = lastName;
        registerRequest.email = email;
        registerRequest.username = email;
        registerRequest.password = password;

        if (userResponseCall == null) {
            userResponseCall = podcastApi.postRegister(registerRequest);
            updateProgress();
            userResponseCall.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    userResponseCall = null;
                    updateProgress();

                    if (response.isSuccessful()) {
                        UserResponse body = response.body();
                        body.email = email;
                        body.firstName = firstName;
                        body.lastName = lastName;
                        body.username = email;

                        userStorage.save(body);
                        if (registerActivity != null) {
                            registerActivity.registerSuccessful();
                        }
                    } else {
                        Converter<ResponseBody, ErrorResponse> converter = retrofit.responseBodyConverter(ErrorResponse.class, new Annotation[]{});
                        try {
                            ErrorResponse errorResponse = converter.convert(response.errorBody());
                            if (registerActivity != null) {
                                registerActivity.showError(errorResponse.error);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    userResponseCall = null;
                    updateProgress();

                    if (registerActivity != null) {
                        registerActivity.showError(t.getLocalizedMessage());
                    }
                }
            });
        }

    }

    private void updateProgress() {
        if (registerActivity != null) {
            registerActivity.showProgress(userResponseCall != null);
        }
    }
}

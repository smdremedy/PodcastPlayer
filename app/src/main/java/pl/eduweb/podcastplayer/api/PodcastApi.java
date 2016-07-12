package pl.eduweb.podcastplayer.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PodcastApi {


    @Headers({
            "X-Parse-Application-Id: podcastplayereduweb",
            "X-Parse-REST-API-Key: undefined",
            "X-Parse-Revocable-Session: 1"
    })
    @GET("login")
    Call<UserResponse> getLogin(@Query("username") String username, @Query("password") String password);

    @Headers({
            "X-Parse-Application-Id: podcastplayereduweb",
            "X-Parse-REST-API-Key: undefined",
            "X-Parse-Revocable-Session: 1"
    })
    @POST("users")
    Call<UserResponse> postRegister(@Body RegisterRequest request);

}

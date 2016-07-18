package pl.eduweb.podcastplayer.screens.discover;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import pl.eduweb.podcastplayer.UserStorage;
import pl.eduweb.podcastplayer.api.ErrorConverter;
import pl.eduweb.podcastplayer.api.ErrorResponse;
import pl.eduweb.podcastplayer.api.Podcast;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.api.PodcastResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Autor on 2016-07-14.
 */
public class DiscoverManager {

    private final PodcastApi podcastApi;
    private final Bus bus;
    private final UserStorage userStorage;
    private final ErrorConverter errorConverter;
    private Call<PodcastResponse> call;

    private DiscoverFragment discoverFragment;
    private Call<Subscription> subscriptionCall;

    public DiscoverManager(PodcastApi podcastApi, Bus bus, UserStorage userStorage, ErrorConverter errorConverter) {
        this.podcastApi = podcastApi;
        this.bus = bus;
        this.userStorage = userStorage;
        this.errorConverter = errorConverter;
        bus.register(this);
    }

    public void onAttach(DiscoverFragment discoverFragment) {
        this.discoverFragment = discoverFragment;

    }

    public void onStop() {
        this.discoverFragment = null;
    }

    public void loadPodcasts() {
        call = podcastApi.getPodcasts();
        call.enqueue(new Callback<PodcastResponse>() {
            @Override
            public void onResponse(Call<PodcastResponse> call, Response<PodcastResponse> response) {
                if(response.isSuccessful()) {
                    for (Podcast podcast : response.body().results) {
                        Log.d(DiscoverManager.class.getSimpleName(), "Podcast:" + podcast);
                    }

                    if(discoverFragment != null) {
                        discoverFragment.showPodcasts(response.body().results);
                    }
                }
            }

            @Override
            public void onFailure(Call<PodcastResponse> call, Throwable t) {

            }
        });
    }

    @Subscribe
    public void onAddPodcast(AddPocastEvent event) {
        Log.d(DiscoverManager.class.getSimpleName(), "Added:" + event.podcast);
        saveSubscribtion(event.podcast);
    }

    private void saveSubscribtion(Podcast podcast) {
        String userId = userStorage.getUserId();

        Subscription subscription = new Subscription();
        subscription.podcastId = podcast.podcastId;
        subscription.userId = userId;

        subscription.acl = new JsonObject();

        JsonObject aclJson = new JsonObject();
        aclJson.add("read", new JsonPrimitive(true));
        aclJson.add("write", new JsonPrimitive(true));
        subscription.acl.add(userId, aclJson);

        subscriptionCall = podcastApi.postSubscription(subscription, userStorage.getToken());
        subscriptionCall.enqueue(new Callback<Subscription>() {
            @Override
            public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                if(response.isSuccessful()) {
                    if(discoverFragment != null) {
                        discoverFragment.saveSuccessful();
                    }
                } else {
                    ErrorResponse errorResponse = errorConverter.convert(response.errorBody());
                    if(discoverFragment != null && errorResponse != null) {
                        discoverFragment.showError(errorResponse.error);
                    }
                }
            }

            @Override
            public void onFailure(Call<Subscription> call, Throwable t) {

                if(discoverFragment != null) {
                    discoverFragment.showError(t.getLocalizedMessage());
                }
            }
        });

    }
}

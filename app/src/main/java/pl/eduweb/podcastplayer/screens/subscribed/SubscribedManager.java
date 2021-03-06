package pl.eduweb.podcastplayer.screens.subscribed;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.List;

import pl.eduweb.podcastplayer.UserStorage;
import pl.eduweb.podcastplayer.api.Podcast;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.api.PodcastResponse;
import pl.eduweb.podcastplayer.db.DbHelper;
import pl.eduweb.podcastplayer.db.PodcastDao;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Autor on 2016-07-18.
 */
public class SubscribedManager {

    private final PodcastApi podcastApi;
    private final UserStorage userStorage;
    private Call<PodcastResponse> call;

    private SubscribedFragment subscribedFragment;
    private final PodcastDao dao;
    private SortDialogFragment.SortOrder sortOrder = SortDialogFragment.SortOrder.TITLE;

    public SubscribedManager(PodcastApi podcastApi, UserStorage userStorage, PodcastDao dao, Bus bus) {

        this.podcastApi = podcastApi;
        this.userStorage = userStorage;
        this.dao = dao;
        bus.register(this);
    }

    public void onAttach(SubscribedFragment subscribedFragment) {
        this.subscribedFragment = subscribedFragment;
        loadPodcasts(false);
    }

    public void onStop() {
        this.subscribedFragment = null;
    }

    public void loadPodcasts(boolean forceRefresh) {
        List<PodcastInDb> podcastInDbs = null;


        podcastInDbs = dao.getSubscribedPodcasts(userStorage.getUserId(), sortOrder);

        if (forceRefresh || podcastInDbs == null || podcastInDbs.isEmpty()) {

            loadPodcastsFromServer();

        } else {


            showPodcasts(podcastInDbs);

        }
    }

    private void showPodcasts(List<PodcastInDb> podcastInDbs) {

        if (subscribedFragment != null) {
            subscribedFragment.showPodcasts(podcastInDbs);
        }
    }

    private void loadPodcastsFromServer() {
        String where = "{\"podcastId\":{\"$select\":{\"query\":" +
                "{\"className\":\"Subscription\",\"where\":{\"userId\": \"%s\"}},\"key\":\"podcastId\"}}}";
        call = podcastApi.getSubscribedPodcasts(String.format(where, userStorage.getUserId()), userStorage.getToken());
        call.enqueue(new Callback<PodcastResponse>() {
            @Override
            public void onResponse(Call<PodcastResponse> call, Response<PodcastResponse> response) {
                if (response.isSuccessful()) {


                    for (Podcast podcast : response.body().results) {

                        createOrUpdate(podcast);
                    }


                    List<PodcastInDb> podcastInDbs = dao.getSubscribedPodcasts(userStorage.getUserId(), sortOrder);
                    showPodcasts(podcastInDbs);


                }
            }

            @Override
            public void onFailure(Call<PodcastResponse> call, Throwable t) {

                if (subscribedFragment != null) {
                    subscribedFragment.showError(t.getLocalizedMessage());
                }

            }
        });
    }

    private void createOrUpdate(Podcast podcast) {

        try {
            PodcastInDb podcastInDb = dao.getPodcastById(podcast.podcastId, userStorage.getUserId());
            if (podcastInDb == null) {
                dao.create(PodcastInDb.fromPodcast(podcast, userStorage.getUserId()));
            } else {
                podcastInDb.numberOfEpisodes = podcast.numberOfEpisodes;
                podcastInDb.thumbUrl = podcast.thumbUrl;
                podcastInDb.fullUrl = podcast.fullUrl;
                podcastInDb.title = podcast.title;
                podcastInDb.description = podcast.description;

                dao.update(podcastInDb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onSortOrderChanged(SortOrderChangedEvent event) {
        this.sortOrder = event.sortOrder;
        loadPodcasts(false);
    }

    public SortDialogFragment.SortOrder getSortOrder() {
        return sortOrder;
    }
}

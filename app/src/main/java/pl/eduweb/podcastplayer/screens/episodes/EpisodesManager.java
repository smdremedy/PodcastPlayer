package pl.eduweb.podcastplayer.screens.episodes;

import pl.eduweb.podcastplayer.api.EpisodesResponse;
import pl.eduweb.podcastplayer.api.PodcastApi;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodesManager {

    private EpisodesActivity episodesActivity;
    private PodcastInDb podcast;

    private final PodcastApi podcastApi;
    private Call<EpisodesResponse> call;

    public EpisodesManager(PodcastApi podcastApi) {
        this.podcastApi = podcastApi;
    }

    public void onAttach(EpisodesActivity episodesActivity) {

        this.episodesActivity = episodesActivity;
    }

    public void onStop() {
        this.episodesActivity = null;
    }

    public void setPodcast(PodcastInDb podcast) {
        this.podcast = podcast;
    }

    public void loadEpisodes() {
        String where = String.format("{\"podcastId\":%d}", podcast.podcastId);
        call = podcastApi.getEpisodesByPodcastId(where);

        call.enqueue(new Callback<EpisodesResponse>() {
            @Override
            public void onResponse(Call<EpisodesResponse> call, Response<EpisodesResponse> response) {
                if(response.isSuccessful()) {
                    if(episodesActivity != null) {
                        episodesActivity.showEpisodes(response.body().results);
                    }
                }
            }

            @Override
            public void onFailure(Call<EpisodesResponse> call, Throwable t) {

            }
        });

    }
}

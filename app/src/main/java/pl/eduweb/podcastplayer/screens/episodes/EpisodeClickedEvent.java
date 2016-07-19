package pl.eduweb.podcastplayer.screens.episodes;

import pl.eduweb.podcastplayer.api.Episode;

public class EpisodeClickedEvent {
    public final Episode episode;

    public EpisodeClickedEvent(Episode episode) {
        this.episode = episode;
    }
}

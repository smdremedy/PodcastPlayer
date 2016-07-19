package pl.eduweb.podcastplayer.screens.subscribed;

import pl.eduweb.podcastplayer.db.PodcastInDb;

public class PodcastClickedEvent {
    public final PodcastInDb podcast;

    public PodcastClickedEvent(PodcastInDb podcast) {
        this.podcast = podcast;
    }
}

package pl.eduweb.podcastplayer.screens.discover;

import pl.eduweb.podcastplayer.api.Podcast;

/**
 * Created by Autor on 2016-07-14.
 */
public class AddPocastEvent {
    public final Podcast podcast;

    public AddPocastEvent(Podcast podcast) {
        this.podcast = podcast;
    }
}

package pl.eduweb.podcastplayer.api;

import java.io.Serializable;

public class Episode implements Serializable {

    public String title;
    public String description;
    public String audioUrl;
    public int duration;
}

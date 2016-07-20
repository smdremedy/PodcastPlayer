package pl.eduweb.podcastplayer.service;

public class PlayerStateChangedEvent {

    public final PodcastPlayerEngine.PlayerState playerState;

    public PlayerStateChangedEvent(PodcastPlayerEngine.PlayerState playerState) {
        this.playerState = playerState;
    }
}

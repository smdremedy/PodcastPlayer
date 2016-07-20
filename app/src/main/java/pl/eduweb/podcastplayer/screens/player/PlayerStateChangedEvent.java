package pl.eduweb.podcastplayer.screens.player;

public class PlayerStateChangedEvent {

    private final PodcastPlayerEngine.PlayerState playerState;

    public PlayerStateChangedEvent(PodcastPlayerEngine.PlayerState playerState) {
        this.playerState = playerState;
    }
}

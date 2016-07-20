package pl.eduweb.podcastplayer.screens.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.eduweb.podcastplayer.api.Episode;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import timber.log.Timber;

public class PodcastPlayerEngine implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    public static final int UPDATE_TIME_WHAT = 1;

    public enum PlayerState {
        LOADING, PLAYING, PAUSED, STOPPED
    }

    private final MediaPlayer mediaPlayer;
    private final ScheduledExecutorService scheduleTaskExecutor;
    private final Bus bus;

    private PlayerState playerState = PlayerState.STOPPED;

    private PodcastInDb podcast;
    private Episode episode;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == UPDATE_TIME_WHAT) {
                bus.post(new TimeUpdatedEvent());
                return true;
            }
            return false;
        }
    });


    public PodcastPlayerEngine(Context context, Bus bus) {
        this.bus = bus;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
    }

    public void prepareEpisode(Episode episode, PodcastInDb podcast) {
        this.podcast = podcast;
        this.episode = episode;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(episode.audioUrl);
            mediaPlayer.prepareAsync();
            setPlayerState(PlayerState.LOADING);
        } catch (IOException e) {
            Timber.e(e, "Media player initialization error");
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mediaPlayer.start();
        setPlayerState(PlayerState.PLAYING);

        startTimer();
    }

    private void startTimer() {
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                handler.sendEmptyMessage(UPDATE_TIME_WHAT);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        setPlayerState(PlayerState.STOPPED);
    }

    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public void pause() {
        mediaPlayer.pause();
        setPlayerState(PlayerState.PAUSED);
    }

    private void resume() {
        mediaPlayer.start();
        setPlayerState(PlayerState.PLAYING);

    }

    public void stop() {
        mediaPlayer.stop();
        setPlayerState(PlayerState.STOPPED);
    }

    private void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        bus.post(new PlayerStateChangedEvent(playerState));
    }

    public int getPlayTime() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    public Episode getEpisode() {
        return episode;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    public PodcastInDb getPodcast() {
        return podcast;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}
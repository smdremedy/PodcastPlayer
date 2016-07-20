package pl.eduweb.podcastplayer.screens.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Episode;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import pl.eduweb.podcastplayer.service.PlayerService;
import pl.eduweb.podcastplayer.service.PlayerStateChangedEvent;
import pl.eduweb.podcastplayer.service.TimeUpdatedEvent;

public class PlayerActivity extends AppCompatActivity {

    public static final String EPISODE = "episode";
    public static final String PODCAST = "podcast";

    @BindView(R.id.playerCoverImageView)
    ImageView playerCoverImageView;
    @BindView(R.id.playerSeekBar)
    SeekBar playerSeekBar;
    @BindView(R.id.playerEpisodeLengthTextView)
    TextView playerEpisodeLengthTextView;
    @BindView(R.id.timeLayout)
    LinearLayout timeLayout;
    @BindView(R.id.playerCurrentTimeTextView)
    TextView playerCurrentTimeTextView;
    @BindView(R.id.playerBackImageButton)
    ImageButton playerBackImageButton;
    @BindView(R.id.playerPlayImageButton)
    ImageButton playerPlayImageButton;
    @BindView(R.id.playerFwdImageButton)
    ImageButton playerFwdImageButton;

    @Inject
    Bus bus;

    private PlayerService playerService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) service;
            playerService = playerBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerService = null;

        }
    };

    private void refresh() {
        if(playerService != null) {
            Episode episode = playerService.getEpisode();
            getSupportActionBar().setTitle(episode.title);
            PodcastInDb podcast = playerService.getPodcast();
            Glide.with(this)
                    .load(podcast.fullUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(playerCoverImageView);

            playerEpisodeLengthTextView.setText(formatTime(episode.duration));
            playerCurrentTimeTextView.setText(formatTime(playerService.getPlayTime()));

        }
    }

    private String formatTime(long time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        App.component.inject(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.playerPlayImageButton)
    public void playClicked() {

        playerService.playOrPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
        if(playerService != null) {
            unbindService(serviceConnection);
        }
    }

    @Subscribe
    public void onPlayerStateChanged(PlayerStateChangedEvent event) {
        refresh();
    }

    @Subscribe
    public void onTimeUpdated(TimeUpdatedEvent event) {
        refresh();
    }
}

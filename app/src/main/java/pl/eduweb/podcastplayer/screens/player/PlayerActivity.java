package pl.eduweb.podcastplayer.screens.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Episode;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import pl.eduweb.podcastplayer.screens.episodes.EpisodeClickedEvent;

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

    private PodcastPlayerEngine playerEngine;

    @Inject
    Bus bus;
    private PodcastInDb podcast;
    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        App.component.inject(this);

        podcast = (PodcastInDb) getIntent().getSerializableExtra(PODCAST);
        episode = (Episode) getIntent().getSerializableExtra(EPISODE);

        playerEngine = new PodcastPlayerEngine(this, bus);
    }

    public static void start(Activity activity, Episode episode, PodcastInDb podcast) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(EPISODE, episode);
        intent.putExtra(PODCAST, podcast);
        activity.startActivity(intent);
    }

    @OnClick(R.id.playerPlayImageButton)
    public void playClicked() {

        playerEngine.prepareEpisode(episode, podcast);


    }
}

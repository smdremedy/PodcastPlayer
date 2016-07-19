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

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.screens.episodes.EpisodeClickedEvent;

public class PlayerActivity extends AppCompatActivity {

    public static final String EPISODE = "episode";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
    }

    public static void start(Activity activity, EpisodeClickedEvent event) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(EPISODE, event.episode);
        activity.startActivity(intent);
    }
}

package pl.eduweb.podcastplayer.screens.player;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.screens.episodes.EpisodeClickedEvent;
import pl.eduweb.podcastplayer.screens.episodes.EpisodesActivity;

public class PlayerActivity extends AppCompatActivity {

    public static final String EPISODE = "episode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }

    public static void start(Activity activity, EpisodeClickedEvent event) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(EPISODE, event.episode);
        activity.startActivity(intent);
    }
}

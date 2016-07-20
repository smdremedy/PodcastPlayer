package pl.eduweb.podcastplayer.screens.episodes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.TriStateRecyclerView;
import pl.eduweb.podcastplayer.api.Episode;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import pl.eduweb.podcastplayer.screens.player.PlayerActivity;
import pl.eduweb.podcastplayer.service.PlayerService;
import timber.log.Timber;

public class EpisodesActivity extends AppCompatActivity {

    public static final String PODCAST = "podcast";

    @Inject
    EpisodesManager episodesManager;
    @BindView(R.id.episodesRecyclerView)
    TriStateRecyclerView episodesRecyclerView;
    @BindView(R.id.episodesEmptyView)
    TextView episodesEmptyView;
    @BindView(R.id.episodesProgressBar)
    ProgressBar episodesProgressBar;
    private EpisodesAdapter adapter;

    @Inject
    Bus bus;
    private PodcastInDb podcastInDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        episodesRecyclerView.setLoadingView(episodesProgressBar);
        episodesRecyclerView.setEmptyView(episodesEmptyView);

        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        podcastInDb = (PodcastInDb) getIntent().getSerializableExtra(PODCAST);
        Timber.d("Podcast:%s", podcastInDb);

        App.component.inject(this);
        episodesManager.setPodcast(podcastInDb);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
        episodesManager.onAttach(this);
        episodesManager.loadEpisodes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
        episodesManager.onStop();
    }

    public static void start(Activity activity, PodcastInDb podcast) {
        Intent intent = new Intent(activity, EpisodesActivity.class);
        intent.putExtra(PODCAST, podcast);
        activity.startActivity(intent);
    }

    public void showEpisodes(List<Episode> results) {

        if (adapter == null) {
            adapter = new EpisodesAdapter();
            episodesRecyclerView.setAdapter(adapter);
        }
        adapter.setEpisodes(results);



    }

    @Subscribe
    public void onEpisodeClicked(EpisodeClickedEvent event) {

        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction(PlayerService.ACTION_PLAY);
        intent.putExtra(PlayerService.EPISODE_EXTRA, event.episode);
        intent.putExtra(PlayerService.PODCAST_EXTRA, podcastInDb);
        startService(intent);

        PlayerActivity.start(this);

    }
}

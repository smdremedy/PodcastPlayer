package pl.eduweb.podcastplayer.screens.episodes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Episode;

public class AdapterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.episodeTitleTextView)
    TextView episodeTitleTextView;
    @BindView(R.id.episodeLengthTextView)
    TextView episodeLengthTextView;

    private Episode episode;

    @Inject
    Bus bus;

    public AdapterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        App.component.inject(this);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.post(new EpisodeClickedEvent(episode));
            }
        });
    }


    public void setEpisode(Episode episode) {
        this.episode = episode;

        episodeTitleTextView.setText(episode.title);
        int minutes = episode.duration / 60;
        int seconds = episode.duration % 60;
        String length = episodeLengthTextView.getResources().getString(R.string.episode_length, minutes, seconds);
        episodeLengthTextView.setText(length);

    }

}

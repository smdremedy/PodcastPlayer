package pl.eduweb.podcastplayer.screens.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Podcast;

/**
 * Created by Autor on 2016-07-14.
 */
public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverViewHolder> {

    private final Bus bus;
    private List<Podcast> podcasts = new ArrayList<>();

    public DiscoverAdapter(Bus bus) {

        this.bus = bus;
    }

    @Override
    public DiscoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new DiscoverViewHolder(layoutInflater.inflate(R.layout.item_discover, parent, false), bus);
    }

    @Override
    public void onBindViewHolder(DiscoverViewHolder holder, int position) {

        holder.setPodcast(podcasts.get(position));
    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }


    public void setPodcast(List<Podcast> results) {
        podcasts.clear();
        podcasts.addAll(results);
        notifyDataSetChanged();
    }
}

class DiscoverViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.podcastCoverImageView)
    ImageView podcastCoverImageView;
    @BindView(R.id.podcastNameTextView)
    TextView podcastNameTextView;
    @BindView(R.id.podcastEpisodesCountTextView)
    TextView podcastEpisodesCountTextView;
    @BindView(R.id.podcastAddImageButton)
    ImageButton podcastAddImageButton;

    private final Bus bus;
    private Podcast podcast;

    public DiscoverViewHolder(View itemView, Bus bus) {
        super(itemView);
        this.bus = bus;
        ButterKnife.bind(this, itemView);
    }

    public void setPodcast(Podcast podcast) {
        this.podcast = podcast;
        podcastNameTextView.setText(podcast.title);
        String episodes = podcastEpisodesCountTextView.getResources().getString(R.string.episodes_count, podcast.numberOfEpisodes);
        podcastEpisodesCountTextView.setText(episodes);
        Glide.with(podcastCoverImageView.getContext())
                .load(podcast.thumbUrl)
                .placeholder(R.drawable.placeholder)
                .into(podcastCoverImageView);
    }

    @OnClick(R.id.podcastAddImageButton)
    public void addPodcast() {

        bus.post(new AddPocastEvent(podcast));


    }
}

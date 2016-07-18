package pl.eduweb.podcastplayer.screens.subscribed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Podcast;
import pl.eduweb.podcastplayer.db.PodcastInDb;

/**
 * Created by Autor on 2016-07-18.
 */
public class SubscribedAdapter extends RecyclerView.Adapter<SubscribedViewHolder> {

    private List<PodcastInDb> podcasts = new ArrayList<>();

    @Override
    public SubscribedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new SubscribedViewHolder(layoutInflater.inflate(R.layout.item_subscribed, parent, false));
    }

    @Override
    public void onBindViewHolder(SubscribedViewHolder holder, int position) {

        holder.setPodcast(podcasts.get(position));
    }

    public void setPodcasts(List<PodcastInDb> podcasts) {
        this.podcasts.clear();
        this.podcasts.addAll(podcasts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }


}

class SubscribedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.subscribedCoverImageView)
    ImageView subscribedCoverImageView;
    @BindView(R.id.subscibedTitleTextView)
    TextView subscibedTitleTextView;

    public SubscribedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPodcast(PodcastInDb podcast) {
        subscibedTitleTextView.setText(podcast.title);
        Glide.with(subscribedCoverImageView.getContext())
                .load(podcast.thumbUrl)
                .placeholder(R.drawable.placeholder)
                .into(subscribedCoverImageView);

    }
}
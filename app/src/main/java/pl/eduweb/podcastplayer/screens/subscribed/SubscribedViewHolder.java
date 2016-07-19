package pl.eduweb.podcastplayer.screens.subscribed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.db.PodcastInDb;

public class SubscribedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.subscribedCoverImageView)
    ImageView subscribedCoverImageView;
    @BindView(R.id.subscibedTitleTextView)
    TextView subscibedTitleTextView;

    @Inject
    Bus bus;

    private PodcastInDb podcast;

    public SubscribedViewHolder(View itemView) {
        super(itemView);
        App.component.inject(this);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bus.post(new PodcastClickedEvent(podcast));
            }
        });
    }

    public void setPodcast(PodcastInDb podcast) {
        this.podcast = podcast;
        subscibedTitleTextView.setText(podcast.title);
        Glide.with(subscribedCoverImageView.getContext())
                .load(podcast.thumbUrl)
                .placeholder(R.drawable.placeholder)
                .into(subscribedCoverImageView);

    }
}

package pl.eduweb.podcastplayer.screens.episodes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Episode;

public class EpisodesAdapter extends RecyclerView.Adapter<AdapterViewHolder> {

    private List<Episode> episodes = new ArrayList<>();

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AdapterViewHolder(inflater.inflate(R.layout.item_episode, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {

        holder.setEpisode(episodes.get(position));
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

}


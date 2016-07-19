package pl.eduweb.podcastplayer.screens.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Podcast;

public class DiscoverFragment extends Fragment {

    @BindView(R.id.discoverRecyclerView)
    RecyclerView discoverRecyclerView;

    private DiscoverManager discoverManager;
    private Bus bus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App app = (App) getActivity().getApplication();
        discoverManager = app.getDiscoverManager();
        bus = app.getBus();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        discoverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void onStart() {
        super.onStart();
        discoverManager.onAttach(this);
        discoverManager.loadPodcasts();
    }

    @Override
    public void onStop() {
        super.onStop();
        discoverManager.onStop();
    }

    public void showPodcasts(List<Podcast> results) {
        DiscoverAdapter adapter = new DiscoverAdapter(bus);
        adapter.setPodcast(results);
        discoverRecyclerView.setAdapter(adapter);

    }

    public void saveSuccessful() {
        bus.post(new SwitchToSubscribedEvent());

    }

    public void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}

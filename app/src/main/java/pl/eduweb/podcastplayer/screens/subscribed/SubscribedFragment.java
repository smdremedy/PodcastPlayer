package pl.eduweb.podcastplayer.screens.subscribed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.TriStateRecyclerView;
import pl.eduweb.podcastplayer.db.PodcastInDb;

/**
 * Created by Autor on 2016-07-14.
 */
public class SubscribedFragment extends Fragment {


    @BindView(R.id.subscribedRecyclerView)
    TriStateRecyclerView subscribedRecyclerView;
    @BindView(R.id.subscribedProgressBar)
    ProgressBar subscribedProgressBar;
    @BindView(R.id.subscribedEmptyLayout)
    LinearLayout subscribedEmptyLayout;
    @BindView(R.id.subscribedRefreshLayout)
    SwipeRefreshLayout subscribedRefreshLayout;

    private SubscribedManager subscribedManager;

    @OnClick(R.id.subscribedAddSubscriptionButton)
    public void addSubscriptionClicked() {
        goToSubsribed();

    }

    public void showError(String localizedMessage) {
        subscribedRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), localizedMessage, Toast.LENGTH_SHORT).show();
    }

    public interface Callback {
        void goToDiscover();
    }

    private Callback callback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscribedManager.onAttach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        subscribedManager.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        subscribedManager = ((App) getActivity().getApplication()).getSubscribedManager();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        subscribedRecyclerView.setEmptyView(subscribedEmptyLayout);
        subscribedRecyclerView.setLoadingView(subscribedProgressBar);

        subscribedRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subscribedManager.loadPodcasts(true);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.subscribed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                goToSubsribed();
                return true;
            case R.id.action_sort:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToSubsribed() {
        App app = (App) getActivity().getApplication();
        app.getBus().post(new AddActionEvent());
    }

    public void showPodcasts(List<PodcastInDb> results) {

        subscribedRefreshLayout.setRefreshing(false);

        SubscribedAdapter subscribedAdapter = new SubscribedAdapter();
        subscribedAdapter.setPodcasts(results);

        subscribedRecyclerView.setAdapter(subscribedAdapter);


    }

}

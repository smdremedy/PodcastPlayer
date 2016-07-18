package pl.eduweb.podcastplayer.screens.subscribed;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.MainActivity;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Podcast;

/**
 * Created by Autor on 2016-07-14.
 */
public class SubscribedFragment extends Fragment {

    public void showPodcasts(List<Podcast> results) {

    }

    private SubscribedManager subscribedManager;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        subscribedManager = ((App)getActivity().getApplication()).getSubscribedManager();
        subscribedManager.loadPodcasts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscribed, container, false);
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
                //callback.goToDiscover();
                App app = (App) getActivity().getApplication();
                app.getBus().post(new AddActionEvent());
                return true;
            case R.id.action_sort:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package pl.eduweb.podcastplayer.di;

import javax.inject.Singleton;

import dagger.Component;
import pl.eduweb.podcastplayer.MainActivity;
import pl.eduweb.podcastplayer.screens.discover.DiscoverFragment;
import pl.eduweb.podcastplayer.screens.episodes.AdapterViewHolder;
import pl.eduweb.podcastplayer.screens.episodes.EpisodesActivity;
import pl.eduweb.podcastplayer.screens.login.LoginActivity;
import pl.eduweb.podcastplayer.screens.register.RegisterActivity;
import pl.eduweb.podcastplayer.screens.subscribed.SortDialogFragment;
import pl.eduweb.podcastplayer.screens.subscribed.SubscribedFragment;
import pl.eduweb.podcastplayer.screens.subscribed.SubscribedViewHolder;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(DiscoverFragment fragment);

    void inject(SortDialogFragment fragment);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(SubscribedFragment fragment);

    void inject(MainActivity activity);

    void inject(EpisodesActivity activity);

    void inject(SubscribedViewHolder holder);

    void inject(AdapterViewHolder holder);
}

package pl.eduweb.podcastplayer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;
import pl.eduweb.podcastplayer.api.Episode;
import pl.eduweb.podcastplayer.api.Podcast;
import pl.eduweb.podcastplayer.db.PodcastInDb;
import pl.eduweb.podcastplayer.screens.player.PlayerActivity;
import timber.log.Timber;

public class PlayerService extends Service {

    public static final String ACTION_PLAY = "pl.eduweb.podcastplayer.PLAY";
    public static final String ACTION_STOP = "pl.eduweb.podcastplayer.STOP";
    public static final String EPISODE_EXTRA = "episode";
    public static final String PODCAST_EXTRA = "podcast";
    public static final int NOTIFICATION_ID = 1;
    private PodcastPlayerEngine playerEngine;
    @Inject
    Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        App.component.inject(this);
        bus.register(this);
        playerEngine = new PodcastPlayerEngine(this, bus);
        Timber.d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timber.d("onStartCommand:%s", intent);
        if (intent != null && intent.getAction() != null){
            if (intent.getAction().equals(ACTION_PLAY)) {
                Episode episode = (Episode) intent.getSerializableExtra(EPISODE_EXTRA);
                PodcastInDb podcast = (PodcastInDb) intent.getSerializableExtra(PODCAST_EXTRA);
                play(episode, podcast);
            } else if(intent.getAction().equals(ACTION_STOP)) {
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        playerEngine.stop();

    }

    @Subscribe
    public void onStateChanged(PlayerStateChangedEvent event) {
        if(event.playerState == PodcastPlayerEngine.PlayerState.PLAYING ||
                event.playerState == PodcastPlayerEngine.PlayerState.PAUSED) {

            Intent intent = new Intent(this, PlayerActivity.class);
            PendingIntent playerPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent stopIntent = new Intent(this, PlayerService.class);
            stopIntent.setAction(ACTION_STOP);
            PendingIntent stopPendingIntent = PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setContentTitle(playerEngine.getEpisode().title);
            builder.setContentText(playerEngine.getPodcast().title);
            builder.setContentIntent(playerPendingIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", stopPendingIntent);

            startForeground(NOTIFICATION_ID, builder.build());
        }
    }

    private IBinder binder = new PlayerBinder();

    public void play(Episode episode, PodcastInDb podcast) {
        playerEngine.prepareEpisode(episode, podcast);

    }

    public class PlayerBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

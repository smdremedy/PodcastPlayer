package pl.eduweb.podcastplayer.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import pl.eduweb.podcastplayer.api.Podcast;

@DatabaseTable(tableName = "podcast", daoClass = PodcastDao.class)
public class PodcastInDb {

    public static PodcastInDb fromPodcast(Podcast podcast, String userId) {
        PodcastInDb podcastInDb = new PodcastInDb();

        podcastInDb.description = podcast.description;
        podcastInDb.title = podcast.title;
        podcastInDb.fullUrl = podcast.fullUrl;
        podcastInDb.thumbUrl = podcast.thumbUrl;
        podcastInDb.numberOfEpisodes = podcast.numberOfEpisodes;
        podcastInDb.podcastId = podcast.podcastId;
        podcastInDb.userId = userId;

        return podcastInDb;
    }

    public class Columns {

        public static final String PODCAST_ID = "podcast_id";
        public static final String NO_OF_FIELDS = "no_of_fields";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String FULL_URL = "full_url";
        public static final String THUMB_URL = "thumb_url";
        public static final String USER_ID = "user_id";
        public static final String ID = "id";
    }


    @DatabaseField(generatedId = true, columnName = Columns.ID)
    public long id;

    @DatabaseField(uniqueCombo = true, columnName = Columns.PODCAST_ID)
    public long podcastId;
    @DatabaseField(uniqueCombo = true, columnName = Columns.USER_ID)
    public String userId;
    @DatabaseField(columnName = Columns.NO_OF_FIELDS)
    public int numberOfEpisodes;
    @DatabaseField(columnName = Columns.TITLE)
    public String title;
    @DatabaseField(columnName = Columns.DESCRIPTION)
    public String description;
    @DatabaseField(columnName = Columns.FULL_URL)
    public String fullUrl;
    @DatabaseField(columnName = Columns.THUMB_URL)
    public String thumbUrl;
}

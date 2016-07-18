package pl.eduweb.podcastplayer.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

public class PodcastDao extends BaseDaoImpl<PodcastInDb, Long> {

    public PodcastDao(Class<PodcastInDb> dataClass) throws SQLException {
        super(dataClass);
    }

    public PodcastDao(ConnectionSource connectionSource, Class<PodcastInDb> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public PodcastDao(ConnectionSource connectionSource, DatabaseTableConfig<PodcastInDb> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }

    public PodcastInDb getPodcastById(long podcastId, String userId) throws SQLException {

        QueryBuilder<PodcastInDb, ?> builder = queryBuilder();
        builder.where().eq(PodcastInDb.Columns.PODCAST_ID, podcastId).and().eq(PodcastInDb.Columns.USER_ID, userId);

        return queryForFirst(builder.prepare());
    }

    public List<PodcastInDb> getSubscribedPodcasts(String userId) throws SQLException {
        return queryForEq(PodcastInDb.Columns.USER_ID,userId);
    }
}

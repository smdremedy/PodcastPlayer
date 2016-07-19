package pl.eduweb.podcastplayer.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.util.List;

import pl.eduweb.podcastplayer.screens.subscribed.SortDialogFragment;

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

    public List<PodcastInDb> getSubscribedPodcasts(String userId, SortDialogFragment.SortOrder sortOrder) {
        try {
            QueryBuilder<PodcastInDb, Long> builder = queryBuilder();

            builder.where().eq(PodcastInDb.Columns.USER_ID,userId);
            builder.orderBy(columnBySortOrder(sortOrder), true);
            return query(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String columnBySortOrder(SortDialogFragment.SortOrder sortOrder) {
        switch (sortOrder) {
            case TITLE:
                return PodcastInDb.Columns.TITLE;
            case NO_OF_EPISODES:
                return PodcastInDb.Columns.NO_OF_EPISODES;
            default:
                throw new IllegalArgumentException("Unknown sort order:" + sortOrder);

        }
    }
}

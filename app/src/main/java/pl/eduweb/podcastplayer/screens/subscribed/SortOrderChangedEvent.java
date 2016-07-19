package pl.eduweb.podcastplayer.screens.subscribed;

public class SortOrderChangedEvent {
    public final SortDialogFragment.SortOrder sortOrder;

    public SortOrderChangedEvent(SortDialogFragment.SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}

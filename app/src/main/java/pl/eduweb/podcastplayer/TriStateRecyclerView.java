package pl.eduweb.podcastplayer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Autor on 2016-07-18.
 */
public class TriStateRecyclerView extends RecyclerView {

    private View emptyView;
    private View loadingView;

    private AdapterDataObserver dataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Adapter adapter = getAdapter();
            boolean hasData = adapter.getItemCount() > 0;
            setVisibility(hasData ? VISIBLE : GONE);
            emptyView.setVisibility(hasData ? GONE : VISIBLE);
        }
    };

    public TriStateRecyclerView(Context context) {
        super(context);
    }

    public TriStateRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TriStateRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        loadingView.setVisibility(adapter == null ? VISIBLE : GONE);
        if(adapter != null) {
            adapter.registerAdapterDataObserver(dataObserver);
            dataObserver.onChanged();

        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }
}

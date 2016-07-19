package pl.eduweb.podcastplayer.screens.subscribed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.squareup.otto.Bus;

import pl.eduweb.podcastplayer.App;
import pl.eduweb.podcastplayer.R;

public class SortDialogFragment extends DialogFragment {

    public static final String SORT_ORDER = "sort_order";
    private Bus bus;

    public enum SortOrder {
        TITLE(0), NO_OF_EPISODES(1);

        private final int which;

        SortOrder(int which) {

            this.which = which;
        }
    }

    public static SortDialogFragment newInstance(SortOrder sortOrder) {

        Bundle args = new Bundle();
        args.putSerializable(SORT_ORDER, sortOrder);
        SortDialogFragment fragment = new SortDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = ((App)getActivity().getApplication()).getBus();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SortOrder sortOrder = (SortOrder) getArguments().getSerializable(SORT_ORDER);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.sort_order_title);
        builder.setSingleChoiceItems(R.array.sort_types, sortOrder.which, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SortOrder sortOrder = SortOrder.values()[which];
                bus.post(new SortOrderChangedEvent(sortOrder));
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}

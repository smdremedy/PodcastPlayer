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

    private Bus bus;

    public enum SortOrder {
        TITLE, NO_OF_EPISODES
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = ((App)getActivity().getApplication()).getBus();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.sort_order_title);
        builder.setSingleChoiceItems(R.array.sort_types, 0, new DialogInterface.OnClickListener() {
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

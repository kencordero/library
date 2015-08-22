package com.kentheken.library.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.kentheken.library.R;

public class PlatformSelectFragment extends DialogFragment {
    private static final String TAG = PlatformSelectFragment.class.getSimpleName();
    private static final String EXTRA_LIST = "platforms";
    public static final String EXTRA_SELECTION = "selection";
    private static boolean[] mSelections;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        String[] platforms = getArguments().getStringArray(EXTRA_LIST);
        mSelections = getArguments().getBooleanArray(EXTRA_SELECTION);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_platform_select)
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            .setMultiChoiceItems(platforms, mSelections,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx, boolean isChecked) {
                        mSelections[idx] = isChecked;
                        Log.i(TAG, "#" + idx + " AFTER: " + mSelections[idx]);
                    }
                })
                // Set the action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Log.i(TAG, "clickPositiveButton");
                        getArguments().putBooleanArray(EXTRA_SELECTION, mSelections);
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "clickNegativeButton");
                        sendResult(Activity.RESULT_CANCELED);
                    }
                });
        return builder.create();
    }

    public static PlatformSelectFragment newInstance(String[] platforms, boolean[] isSelected ) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_LIST, platforms);
        bundle.putBooleanArray(EXTRA_SELECTION, isSelected);
        PlatformSelectFragment fragment = new PlatformSelectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_SELECTION, mSelections);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}

package com.kentheken.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.ArrayList;

public class PlatformSelectDialogFragment extends DialogFragment {
    private static final String TAG = "PlatformSelectDialogFragment";
    private static final String EXTRA_LIST = "platforms";
    public static final String EXTRA_SELECTION = "selection";
    private ArrayList<Integer> mSelectedItems;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        String[] platforms = getArguments().getStringArray(EXTRA_LIST);
        boolean[] selections = getArguments().getBooleanArray(EXTRA_SELECTION);
        mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_platform_select)
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            .setMultiChoiceItems(platforms, selections,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                            for (Integer i : mSelectedItems) {
                                Log.i(TAG, "Selected: " + i);
                            }
                        } else if (mSelectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                            for (Integer i : mSelectedItems) {
                                Log.i(TAG, "Selected: " + i);
                            }
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Log.i(TAG, "clickPositiveButton");
                        getArguments().putIntegerArrayList(EXTRA_SELECTION, mSelectedItems);
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    public static PlatformSelectDialogFragment newInstance(String[] platforms, boolean[] isSelected ) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_LIST, platforms);
        bundle.putBooleanArray(EXTRA_SELECTION, isSelected);
        PlatformSelectDialogFragment fragment = new PlatformSelectDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_SELECTION, mSelectedItems);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}

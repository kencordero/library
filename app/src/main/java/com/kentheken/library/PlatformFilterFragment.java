package com.kentheken.library;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class PlatformFilterFragment extends DialogFragment {
    private static final String TAG = PlatformFilterFragment.class.getSimpleName();
    public static final String EXTRA_LIST = "com.kentheken.library.platforms";
    public static final String EXTRA_PLATFORM = "com.kentheken.library.platform";
    private String mPlatform;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        final String[] platforms = getArguments().getStringArray(EXTRA_LIST);
        Log.i(TAG, "retrieved platform list");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_platform_select)
                .setItems(platforms, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlatform = platforms[which];
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    public static PlatformFilterFragment newInstance(String[] platforms) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_LIST, platforms);
        PlatformFilterFragment fragment = new PlatformFilterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_PLATFORM, mPlatform);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

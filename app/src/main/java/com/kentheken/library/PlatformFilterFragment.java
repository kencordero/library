package com.kentheken.library;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.kentheken.library.models.Platform;

import java.io.Serializable;

public class PlatformFilterFragment extends DialogFragment {
    private static final String TAG = PlatformFilterFragment.class.getSimpleName();
    public static final String EXTRA_PLATFORM = "com.kentheken.library.platform";
    private Platform mPlatform;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        return new Dialog(getActivity());
    }

    public static PlatformFilterFragment newInstance(String[] platforms) {
        Bundle bundle = new Bundle();

        PlatformFilterFragment fragment = new PlatformFilterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_PLATFORM, (Serializable)mPlatform);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
